package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.dinoleon.component.Animation
import com.github.quillraven.dinoleon.component.Animation.Companion.NO_ANIMATION
import com.github.quillraven.dinoleon.component.Animation2D
import com.github.quillraven.dinoleon.component.ImageComponent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.collections.map
import ktx.log.logger

class AnimationSystem(
    private val atlas: TextureAtlas = inject(),
) : IteratingSystem(family { all(Animation, ImageComponent) }) {
    private val cachedAnimations = mutableMapOf<String, Animation2D>()

    override fun onTickEntity(entity: Entity) {
        with(entity[Animation]) {
            entity[ImageComponent].image.drawable = if (nextAnimation != NO_ANIMATION) {
                animation = animation(nextAnimation)
                nextAnimation = NO_ANIMATION
                stateTime = 0f
                animation.playMode = mode
                animation.getKeyFrame(0f)
            } else {
                stateTime += deltaTime
                animation.playMode = mode
                animation.getKeyFrame(stateTime)
            }
        }
    }

    private fun animation(atlasKey: String): com.badlogic.gdx.graphics.g2d.Animation<TextureRegionDrawable> {
        return cachedAnimations.getOrPut(atlasKey) {
            LOG.debug { "Creating new animation $atlasKey" }
            Animation2D(DEFAULT_FRAME_DURATION, atlas.findRegions(atlasKey).map { TextureRegionDrawable(it) })
        }
    }

    companion object {
        private val LOG = logger<AnimationSystem>()
        private const val DEFAULT_FRAME_DURATION = 1 / 12f
    }
}
