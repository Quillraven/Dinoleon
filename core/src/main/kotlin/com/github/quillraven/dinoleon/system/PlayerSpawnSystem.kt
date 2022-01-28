package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.dinoleon.component.*
import com.github.quillraven.dinoleon.component.PhysicComponent.Companion.physicCmpFromImage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.box2d.box
import ktx.box2d.edge

@AllOf(components = [DinoComponent::class])
class PlayerSpawnSystem(private val physicWorld: World) : IteratingSystem() {
    var respawnPlayer = true

    override fun onTick() {
        super.onTick()
        if (respawnPlayer) {
            respawnPlayer = false

            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = Image().apply {
                        setScaling(Scaling.fill)
                        setPosition(1.5f, 1f)
                        setSize(2f, 2f)
                    }
                }
                add<AnimationComponent> { nextAnimation = "dino-blue-run" }
                this.physicCmpFromImage(physicWorld, imageCmp.image) { width, height ->
                    box(width, height)
                    val sensorDistX = 0.5f
                    val sensorH = height + 0.5f
                    edge(sensorDistX, -sensorH, sensorDistX, sensorH) { isSensor = true }
                }
                add<DinoComponent> { life = 5 }
                add<DinoColorComponent> { color = DinoColor.BLUE }
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        if (respawnPlayer) {
            world.remove(entity)
        }
    }
}