package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.ImageComponent
import com.github.quillraven.dinoleon.event.SpawnRemovalEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf(components = [ImageComponent::class])
class DespawnSystem(
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val stage: Stage
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        if (imageCmps[entity].image.x <= -1f) {
            // important to remove entity first before firing the event
            // because the event could cause a "removeAll" call in the GameScreen
            // which also recreates the player entity and therefore
            // and therefore this entity could link to a recycled player instead
            world.remove(entity)
            stage.root.fire(SpawnRemovalEvent())
        }
    }
}
