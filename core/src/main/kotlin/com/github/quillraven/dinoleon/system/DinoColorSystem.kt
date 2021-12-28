package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.Input
import com.github.quillraven.dinoleon.component.AnimationComponent
import com.github.quillraven.dinoleon.component.DinoColor
import com.github.quillraven.dinoleon.component.DinoColorComponent
import com.github.quillraven.dinoleon.component.DinoComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.KtxInputAdapter

@AllOf(components = [DinoComponent::class, DinoColorComponent::class])
class DinoColorSystem(
    private val colorCmps: ComponentMapper<DinoColorComponent>,
    private val aniCmps: ComponentMapper<AnimationComponent>
) : IteratingSystem(enabled = false), KtxInputAdapter {
    private var newColor: DinoColor? = null

    override fun onTickEntity(entity: Entity) {
        val color = newColor
        if (color != null) {
            newColor = null
            colorCmps[entity].color = color
            aniCmps[entity].nextAnimation = "dino-${color.name.lowercase()}-run"
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (!enabled) {
            return false
        }

        newColor = when (keycode) {
            Input.Keys.Q -> DinoColor.RED
            Input.Keys.W -> DinoColor.GREEN
            Input.Keys.E -> DinoColor.BLUE
            Input.Keys.R -> DinoColor.YELLOW
            else -> return false
        }
        return true
    }
}
