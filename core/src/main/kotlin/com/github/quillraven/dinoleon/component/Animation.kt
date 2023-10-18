package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias Animation2D = com.badlogic.gdx.graphics.g2d.Animation<TextureRegionDrawable>

data class Animation(
    var stateTime: Float = 0f,
    var nextAnimation: String = NO_ANIMATION,
    var mode: PlayMode = PlayMode.LOOP,
    var animation: Animation2D = EMPTY_ANIMATION,
) : Component<Animation> {

    override fun type() = Animation

    companion object : ComponentType<Animation>() {
        private val EMPTY_ANIMATION = Animation2D(0f)
        const val NO_ANIMATION = ""
    }
}
