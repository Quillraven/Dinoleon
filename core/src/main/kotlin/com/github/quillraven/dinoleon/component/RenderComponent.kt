package com.github.quillraven.dinoleon.component

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling

data class RenderComponent(
    val image: Image = Image().apply { setScaling(Scaling.fill) }
)
