package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.dinoleon.component.*
import com.github.quillraven.dinoleon.component.PhysicComponent.Companion.physicCmpFromImage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.box
import ktx.box2d.edge

class PlayerSpawnSystem(private val physicWorld: World = inject()) : IteratingSystem(family { all(DinoComponent) }) {
    var respawnPlayer = true

    override fun onTick() {
        super.onTick()
        if (respawnPlayer) {
            respawnPlayer = false

            world.entity {
                it += ImageComponent().apply {
                    image = Image().apply {
                        setScaling(Scaling.fill)
                        setPosition(1.5f, 1f)
                        setSize(2f, 2f)
                    }
                }
                it += Animation(nextAnimation = "dino-blue-run")
                it += physicCmpFromImage(physicWorld, it[ImageComponent].image) { width, height ->
                    box(width, height)
                    val sensorDistX = 0.5f
                    val sensorH = height + 0.5f
                    edge(sensorDistX, -sensorH, sensorDistX, sensorH) { isSensor = true }
                }
                it += DinoComponent(life = 5)
                it += DinoColor(DinoColors.BLUE)
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        if (respawnPlayer) {
            entity.remove()
        }
    }
}