package com.github.quillraven.dinoleon.actor

import com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.dinoleon.screen.GameScreen.Companion.UNIT_SCALE

class ParallaxBackground(
    private val regionsAndSpeed: Array<Pair<TextureRegionDrawable, Vector2>>
) : Actor() {

    init {
        regionsAndSpeed.forEach { it.first.region.texture.setWrap(Repeat, Repeat) }
    }

    override fun act(delta: Float) {
        super.act(delta)
        regionsAndSpeed.forEach { (drawable, speed) ->
            drawable.region.scroll(speed.x * delta, speed.y * delta)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        regionsAndSpeed.forEach {
            val regDrawable = it.first
            regDrawable.draw(batch, 0f, 0f, stage.width, regDrawable.region.regionHeight * UNIT_SCALE)
        }
    }
}
