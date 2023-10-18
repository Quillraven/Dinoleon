package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.Damage
import com.github.quillraven.dinoleon.component.Dino
import com.github.quillraven.dinoleon.event.DinoDamageEvent
import com.github.quillraven.dinoleon.event.DinoDeathEvent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

class DamageSystem(
    private val stage: Stage = inject()
) : IteratingSystem(family { all(Dino, Damage) }) {
    override fun onTickEntity(entity: Entity) {
        val (damage) = entity[Damage]
        with(entity[Dino]) {
            life -= damage
            stage.root.fire(DinoDamageEvent(life))
            if (life <= 0) {
                stage.root.fire(DinoDeathEvent())
            }
            entity.configure { it -= Damage }
        }
    }
}
