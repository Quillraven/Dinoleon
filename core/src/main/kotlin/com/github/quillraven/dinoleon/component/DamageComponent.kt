package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class DamageComponent(var damage: Int = 0) : Component<DamageComponent> {
    override fun type() = DamageComponent

    companion object : ComponentType<DamageComponent>()
}