package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.scenes.scene2d.ui.Image

class ImageComponent : Comparable<ImageComponent> {
    lateinit var image: Image
    var layer = 0

    override fun compareTo(other: ImageComponent): Int {
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
}
