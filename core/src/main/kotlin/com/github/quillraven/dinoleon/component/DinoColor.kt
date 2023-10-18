package com.github.quillraven.dinoleon.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class DinoColors {
    RED, GREEN, BLUE, YELLOW;

    override fun toString(): String {
        return this.name.lowercase()
    }

    companion object {
        fun byOrdinal(ordinal: Int): DinoColors = when (ordinal) {
            0 -> RED
            1 -> GREEN
            2 -> BLUE
            else -> YELLOW
        }
    }
}

data class DinoColor(var color: DinoColors = DinoColors.BLUE) : Component<DinoColor> {
    override fun type() = DinoColor

    companion object : ComponentType<DinoColor>()
}
