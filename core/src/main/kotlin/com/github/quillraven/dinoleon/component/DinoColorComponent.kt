package com.github.quillraven.dinoleon.component

enum class DinoColor {
    RED, GREEN, BLUE, YELLOW;

    override fun toString(): String {
        return this.name.lowercase()
    }

    companion object {
        val size = values().size

        fun byOrdinal(ordinal: Int): DinoColor = when (ordinal) {
            0 -> RED
            1 -> GREEN
            2 -> BLUE
            else -> YELLOW
        }
    }
}

data class DinoColorComponent(var color: DinoColor = DinoColor.BLUE)
