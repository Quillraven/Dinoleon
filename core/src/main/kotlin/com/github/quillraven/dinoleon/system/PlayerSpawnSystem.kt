package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.dinoleon.component.*
import com.github.quillraven.dinoleon.component.PhysicComponent.Companion.physicCmpFromImage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.box
import ktx.box2d.edge

class PlayerSpawnSystem(private val physicWorld: World = inject()) : IteratingSystem(family { all(Dino) }) {
    var respawnPlayer = true

    override fun onTick() {
        super.onTick()
        if (respawnPlayer) {
            respawnPlayer = false

            world.entity {
                it += Image(
                    Image2D(null, Scaling.fill).apply {
                        setPosition(1.5f, 1f)
                        setSize(2f, 2f)
                    }
                )
                it += Animation(nextAnimation = "dino-blue-run")
                it += physicCmpFromImage(physicWorld, it[Image].image) { width, height ->
                    box(width, height)
                    val sensorDistX = 0.5f
                    val sensorH = height + 0.5f
                    edge(sensorDistX, -sensorH, sensorDistX, sensorH) { isSensor = true }
                }
                it += Dino(life = 5)
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