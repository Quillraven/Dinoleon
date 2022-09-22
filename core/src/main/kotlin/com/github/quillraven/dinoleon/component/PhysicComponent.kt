package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import ktx.box2d.BodyDefinition
import ktx.box2d.body

class PhysicComponent(
    val impulse: Vector2 = Vector2()
) : Component<PhysicComponent> {
    lateinit var body: Body

    override fun type() = PhysicComponent

    companion object : ComponentType<PhysicComponent>() {
        fun physicCmpFromImage(
            world: World,
            image: Image,
            fixtureAction: BodyDefinition.(Float, Float) -> Unit
        ): PhysicComponent {
            val x = image.x
            val y = image.y
            val width = image.width
            val height = image.height

            return PhysicComponent().apply {
                body = world.body(BodyDef.BodyType.DynamicBody) {
                    position.set(x + width * 0.5f, y + height * 0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(width, height)
                }
            }
        }

        val onAddPhysic: ComponentHook<PhysicComponent> = { _, entity, physic ->
            physic.body.userData = entity
        }

        val onRemovePhysic: ComponentHook<PhysicComponent> = { _, _, physic ->
            val body = physic.body
            body.world.destroyBody(body)
            body.userData = null
        }
    }
}
