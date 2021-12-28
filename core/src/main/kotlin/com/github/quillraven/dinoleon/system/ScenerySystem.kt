package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.GdxRuntimeException
import com.github.quillraven.dinoleon.actor.ParallaxBackground
import com.github.quillraven.fleks.IntervalSystem
import ktx.math.vec2


class ScenerySystem(private val stage: Stage) : IntervalSystem() {
    private val textures = mapOf(
        "ground" to Texture("ground.png"),
        "bg1" to Texture("bg1.png"),
        "bg2" to Texture("bg2.png"),
        "bg3" to Texture("bg3.png"),
    )
    private val bgdActor = ParallaxBackground(
        arrayOf(
            drawable("bg3") to vec2(0.01f, 0f),
            drawable("bg2") to vec2(0.05f, 0f),
            drawable("bg1") to vec2(0.1f, 0f),
            drawable("ground") to vec2(0.3f, 0f)
        )
    )

    private fun drawable(key: String): TextureRegionDrawable {
        if (key !in textures) {
            throw GdxRuntimeException("No scenery texture loaded for key $key")
        }

        return TextureRegionDrawable(textures[key])
    }

    override fun onTick() {
        stage.addActor(bgdActor)

        // TODO spawn small/big trees
    }

    override fun onDispose() {
        textures.values.forEach { it.dispose() }
    }
}
