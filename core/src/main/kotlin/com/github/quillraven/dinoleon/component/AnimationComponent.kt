package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

data class AnimationComponent(
    var stateTime: Float = 0f,
    var nextAnimation: String = NO_ANIMATION,
    var mode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    var animation: Animation<TextureRegionDrawable> = EMPTY_ANIMATION

    companion object {
        private val EMPTY_ANIMATION = Animation<TextureRegionDrawable>(0f)
        const val NO_ANIMATION = ""
    }
}
