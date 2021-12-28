package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.ImageComponent
import com.github.quillraven.dinoleon.ui.clearActors
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.collection.compareEntity

@AllOf(components = [ImageComponent::class])
class RenderSystem(
    private val stage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>
) : IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
) {
    override fun onTick() {
        super.onTick()
        stage.act(deltaTime)
        stage.draw()
        stage.clearActors()
    }

    override fun onTickEntity(entity: Entity) {
        stage.addActor(imageCmps[entity].image)
    }
}
