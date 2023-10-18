package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias Image2D = com.badlogic.gdx.scenes.scene2d.ui.Image

data class Image(val image: Image2D, var layer: Int = 0) : Component<Image>, Comparable<Image> {
    override fun type() = Image

    override fun compareTo(other: Image): Int {
        val layerDiff = layer.compareTo(other.layer)
        return if (layerDiff != 0) {
            layerDiff
        } else {
            val yDiff = other.image.y.compareTo(image.y)
            if (yDiff != 0) {
                yDiff
            } else {
                other.image.x.compareTo(image.x)
            }
        }
    }

    companion object : ComponentType<Image>()
}
