package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.quillraven.dinoleon.component.Animation
import com.github.quillraven.dinoleon.component.DinoColor
import com.github.quillraven.dinoleon.component.DinoColorComponent
import com.github.quillraven.dinoleon.component.DinoComponent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.app.KtxInputAdapter

class DinoColorSystem : IteratingSystem(
    family = family { all(DinoComponent, DinoColorComponent) },
    enabled = false
), KtxInputAdapter {
    private var newColor: DinoColor? = null
    private val sndChange = Gdx.audio.newSound(Gdx.files.internal("change_color.wav"))

    override fun onTickEntity(entity: Entity) {
        val color = newColor
        val colorCmp = entity[DinoColorComponent]
        if (color != null && color != colorCmp.color) {
            sndChange.play()
            newColor = null
            colorCmp.color = color
            entity[Animation].nextAnimation = "dino-${color.name.lowercase()}-run"
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

    override fun onDispose() {
        sndChange.dispose()
    }
}
