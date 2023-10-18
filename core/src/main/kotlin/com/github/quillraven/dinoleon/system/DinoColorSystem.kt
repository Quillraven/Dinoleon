package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.quillraven.dinoleon.component.Animation
import com.github.quillraven.dinoleon.component.Dino
import com.github.quillraven.dinoleon.component.DinoColor
import com.github.quillraven.dinoleon.component.DinoColors
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.app.KtxInputAdapter

class DinoColorSystem : IteratingSystem(
    family = family { all(Dino, DinoColor) },
    enabled = false
), KtxInputAdapter {
    private var newColor: DinoColors? = null
    private val sndChange = Gdx.audio.newSound(Gdx.files.internal("change_color.wav"))

    override fun onTickEntity(entity: Entity) {
        with(entity[DinoColor]) {
            newColor?.let { nextColor ->
                if (nextColor == color) return // no color change -> do nothing

                sndChange.play()
                color = nextColor
                newColor = null
                entity[Animation].nextAnimation = "dino-${color.name.lowercase()}-run"
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (!enabled) {
            return false
        }

        newColor = when (keycode) {
            Input.Keys.Q -> DinoColors.RED
            Input.Keys.W -> DinoColors.GREEN
            Input.Keys.E -> DinoColors.BLUE
            Input.Keys.R -> DinoColors.YELLOW
            else -> return false
        }
        return true
    }

    override fun onDispose() {
        sndChange.dispose()
    }
}
