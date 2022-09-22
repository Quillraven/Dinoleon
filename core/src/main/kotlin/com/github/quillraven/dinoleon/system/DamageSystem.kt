package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.DamageComponent
import com.github.quillraven.dinoleon.component.DinoComponent
import com.github.quillraven.dinoleon.event.DinoDamageEvent
import com.github.quillraven.dinoleon.event.DinoDeathEvent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

class DamageSystem(
    private val stage: Stage = inject()
) : IteratingSystem(family { all(DinoComponent, DamageComponent) }) {
    override fun onTickEntity(entity: Entity) {
        val dinoCmp = entity[DinoComponent]
        val damageCmp = entity[DamageComponent]

        dinoCmp.life -= damageCmp.damage
        stage.root.fire(DinoDamageEvent(dinoCmp.life))
        if (dinoCmp.life <= 0) {
            stage.root.fire(DinoDeathEvent())
        }

        entity.configure { it -= DamageComponent }
    }
}
