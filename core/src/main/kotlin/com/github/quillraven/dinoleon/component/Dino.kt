package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Dino(var life: Int = 5) : Component<Dino> {
    override fun type() = Dino

    companion object : ComponentType<Dino>()
}
