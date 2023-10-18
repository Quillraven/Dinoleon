package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Damage(var damage: Int = 0) : Component<Damage> {
    override fun type() = Damage

    companion object : ComponentType<Damage>()
}