package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class DinoComponent(var life: Int = 5) : Component<DinoComponent> {
    override fun type() = DinoComponent

    companion object : ComponentType<DinoComponent>()
}
