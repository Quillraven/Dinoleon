@file:JvmName("Lwjgl3Launcher")

package com.github.quillraven.dinoleon.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.quillraven.dinoleon.Dinoleon

fun main() {
    Lwjgl3Application(Dinoleon(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Dinoleon")
        setWindowedMode(640, 360)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
        useVsync(false)
    })
}
