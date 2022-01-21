package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.dinoleon.component.*
import com.github.quillraven.fleks.*
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

private data class Collision(val dino: Entity, val wall: Entity) {
    companion object {
        fun fromContact(contact: Contact, dinoCmps: ComponentMapper<DinoComponent>): Collision? {
            val entityA = contact.fixtureA.body.userData as Entity
            val entityB = contact.fixtureB.body.userData as Entity

            return if (entityA in dinoCmps) {
                Collision(entityA, entityB)
            } else if (entityB in dinoCmps) {
                Collision(entityB, entityA)
            } else {
                return null
            }
        }
    }
}

@AllOf(components = [PhysicComponent::class, ImageComponent::class])
class PhysicSystem(
    private val physicWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val dinoCmps: ComponentMapper<DinoComponent>,
    private val damageCmps: ComponentMapper<DamageComponent>,
    private val colorCmps: ComponentMapper<DinoColorComponent>
) : IteratingSystem(interval = Fixed(1 / 60f)), ContactListener {
    override fun onUpdate() {
        if (physicWorld.autoClearForces) {
            LOG.error { "AutoClearForces must be set to false to guarantee a correct physic step behavior." }
            physicWorld.autoClearForces = false
        }
        super.onUpdate()
        physicWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicWorld.step(deltaTime, 6, 2)
    }

    // store position before world update for smooth interpolated rendering
    override fun onTickEntity(entity: Entity) {
        val imageCmp = imageCmps[entity]
        val physicCmp = physicCmps[entity]
        val (bodyX, bodyY) = physicCmp.body.position

        imageCmp.image.run {
            setPosition(
                bodyX - width * 0.5f,
                bodyY - height * 0.5f
            )
        }

        if (!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }
    }

    // interpolate between position before world step and real position after world step for smooth rendering
    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val imageCmp = imageCmps[entity]
        val physicCmp = physicCmps[entity]

        imageCmp.image.run {
            val prevX = x
            val prevY = y
            val (bodyX, bodyY) = physicCmp.body.position

            setPosition(
                MathUtils.lerp(prevX, bodyX - width * 0.5f, alpha),
                MathUtils.lerp(prevY, bodyY - height * 0.5f, alpha)
            )
        }
    }

    override fun beginContact(contact: Contact) {
        if (contact.fixtureA.isSensor && contact.fixtureB.isSensor) {
            val (dino, wall) = Collision.fromContact(contact, dinoCmps) ?: return

            val dinoColor = colorCmps[dino]
            val wallColor = colorCmps[wall]
            if (dinoColor != wallColor) {
                if (dino in damageCmps) {
                    damageCmps[dino].damage++
                } else {
                    configureEntity(dino) { damageCmps.add(it) { damage = 1 } }
                }
            }
        }
    }

    override fun endContact(contact: Contact) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) = Unit

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit

    companion object {
        private val LOG = logger<PhysicSystem>()
    }
}
