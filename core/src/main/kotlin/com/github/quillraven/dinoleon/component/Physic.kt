package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2
import com.badlogic.gdx.physics.box2d.World as PhysicWorld

data class Physic(
    val body: Body,
    val impulse: Vector2,
) : Component<Physic> {

    override fun type() = Physic

    override fun World.onAdd(entity: Entity) {
        this@Physic.body.userData = entity
    }

    override fun World.onRemove(entity: Entity) {
        val body = this@Physic.body
        body.world.destroyBody(body)
        body.userData = null
    }

    companion object : ComponentType<Physic>() {
        fun physicCmpFromImage(
            world: PhysicWorld,
            image: Image,
            impulseX: Float = 0f,
            impulseY: Float = 0f,
            fixtureAction: BodyDefinition.(Float, Float) -> Unit
        ): Physic {
            val x = image.x
            val y = image.y
            val width = image.width
            val height = image.height

            return Physic(
                body = world.body(BodyDef.BodyType.DynamicBody) {
                    position.set(x + width * 0.5f, y + height * 0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(width, height)
                },
                impulse = vec2(impulseX, impulseY)
            )
        }
    }
}
