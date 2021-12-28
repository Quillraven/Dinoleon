package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.math.Vector2

data class TransformComponent(
    val position: Vector2 = Vector2(),
    val size: Vector2 = Vector2(),
    var layer: Layer = Layer.OBJECTS
) : Comparable<TransformComponent> {
    override fun compareTo(other: TransformComponent): Int {
        val layerDiff = layer.ordinal.compareTo(other.layer.ordinal)
        return if (layerDiff != 0) {
            layerDiff
        } else {
            other.position.y.compareTo(position.y)
        }
    }

    companion object {
        enum class Layer {
            BACKGROUND, OBJECTS, FOREGROUND
        }
    }
}
