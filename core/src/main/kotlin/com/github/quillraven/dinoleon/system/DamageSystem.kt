package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.DamageComponent
import com.github.quillraven.dinoleon.component.DinoComponent
import com.github.quillraven.dinoleon.event.DinoDamageEvent
import com.github.quillraven.dinoleon.event.DinoDeathEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf(components = [DinoComponent::class, DamageComponent::class])
class DamageSystem(
    private val dinoCmps: ComponentMapper<DinoComponent>,
    private val damageCmps: ComponentMapper<DamageComponent>,
    private val stage: Stage
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val dinoCmp = dinoCmps[entity]
        val damageCmp = damageCmps[entity]

        dinoCmp.life -= damageCmp.damage
        stage.root.fire(DinoDamageEvent(dinoCmp.life))
        if (dinoCmp.life <= 0) {
            stage.root.fire(DinoDeathEvent())
        }

        configureEntity(entity) { damageCmps.remove(it) }
    }
}
