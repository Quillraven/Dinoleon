package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class AnimationComponent(
    var stateTime: Float = 0f,
    var nextAnimation: String = NO_ANIMATION,
    var mode: Animation.PlayMode = Animation.PlayMode.LOOP
) : Component<AnimationComponent> {
    var animation: Animation<TextureRegionDrawable> = EMPTY_ANIMATION

    override fun type() = AnimationComponent

    companion object : ComponentType<AnimationComponent>() {
        private val EMPTY_ANIMATION = Animation<TextureRegionDrawable>(0f)
        const val NO_ANIMATION = ""
    }
}
