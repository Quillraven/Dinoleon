package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.github.quillraven.dinoleon.component.*
import com.github.quillraven.dinoleon.system.Collision.Companion.fromContact
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

private data class Collision(val dino: Entity, val wall: Entity) {
    companion object {
        fun IteratingSystem.fromContact(contact: Contact): Collision? {
            val entityA = contact.fixtureA.body.userData as Entity
            val entityB = contact.fixtureB.body.userData as Entity

            return if (entityA has Dino) {
                Collision(entityA, entityB)
            } else if (entityB has Dino) {
                Collision(entityB, entityA)
            } else {
                return null
            }
        }
    }
}

class PhysicSystem(
    private val physicWorld: World = inject(),
) : IteratingSystem(
    family = family { all(PhysicComponent, ImageComponent) },
    interval = Fixed(1 / 60f)
), ContactListener {
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
        val imageCmp = entity[ImageComponent]
        val physicCmp = entity[PhysicComponent]
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
        val imageCmp = entity[ImageComponent]
        val physicCmp = entity[PhysicComponent]

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
            val (dino, wall) = fromContact(contact) ?: return

            val (dinoColor) = dino[DinoColor]
            val (wallColor) = wall[DinoColor]
            if (dinoColor != wallColor) {
                dino.configure {
                    it.getOrAdd(Damage) { Damage(0) }.damage++
                }
            }
        }
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) = Unit

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit

    companion object {
        private val LOG = logger<PhysicSystem>()
    }
}
