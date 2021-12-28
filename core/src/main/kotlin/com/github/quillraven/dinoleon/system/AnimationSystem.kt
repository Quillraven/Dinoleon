package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.dinoleon.component.AnimationComponent
import com.github.quillraven.dinoleon.component.AnimationComponent.Companion.NO_ANIMATION
import com.github.quillraven.dinoleon.component.RenderComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.collections.map

@AllOf(components = [AnimationComponent::class, RenderComponent::class])
class AnimationSystem(
    private val atlas: TextureAtlas,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val renderCmps: ComponentMapper<RenderComponent>
) : IteratingSystem() {
    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val aniCmp = animationCmps[entity]

        renderCmps[entity].image.drawable = if (aniCmp.nextAnimation != NO_ANIMATION) {
            aniCmp.run {
                animation = animation(aniCmp.nextAnimation)
                nextAnimation = NO_ANIMATION
                stateTime = 0f
                animation.playMode = mode
                animation.getKeyFrame(0f)
            }
        } else {
            aniCmp.run {
                stateTime += deltaTime
                animation.playMode = mode
                animation.getKeyFrame(aniCmp.stateTime)
            }
        }
    }

    private fun animation(atlasKey: String): Animation<TextureRegionDrawable> {
        return cachedAnimations.getOrPut(atlasKey) {
            Animation(DEFAULT_FRAME_DURATION, atlas.findRegions(atlasKey).map { TextureRegionDrawable(it) })
        }
    }

    companion object {
        private const val DEFAULT_FRAME_DURATION = 1 / 12f
    }
}
