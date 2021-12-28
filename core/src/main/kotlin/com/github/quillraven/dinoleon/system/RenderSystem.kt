package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.RenderComponent
import com.github.quillraven.dinoleon.component.TransformComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.collection.compareEntity

@AllOf(components = [TransformComponent::class, RenderComponent::class])
class RenderSystem(
    private val stage: Stage,
    private val transformCmps: ComponentMapper<TransformComponent>,
    private val renderCmps: ComponentMapper<RenderComponent>
) : IteratingSystem(
    comparator = compareEntity { e1, e2 -> transformCmps[e1].compareTo(transformCmps[e2]) }
) {
    override fun onTick() {
        super.onTick()
        stage.act(deltaTime)
        stage.draw()
        stage.clear()
    }

    override fun onTickEntity(entity: Entity) {
        val transform = transformCmps[entity]
        stage.addActor(
            renderCmps[entity].image.apply {
                setPosition(transform.position.x, transform.position.y)
                setSize(transform.size.x, transform.size.y)
            }
        )
    }
}
