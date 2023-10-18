package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.dinoleon.component.Image
import com.github.quillraven.dinoleon.ui.clearActors
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy

class RenderSystem(
    private val stage: Stage = inject(),
) : IteratingSystem(
    family = family { all(Image) },
    comparator = compareEntityBy(Image)
) {
    override fun onTick() {
        super.onTick()
        stage.act(deltaTime)
        stage.draw()
        stage.clearActors()
    }

    override fun onTickEntity(entity: Entity) {
        stage.addActor(entity[Image].image)
    }
}
