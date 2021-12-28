package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem

class DebugSystem(
    // TODO how to create and dispose renderer within system
    // TODO how to exclude system via annotation (@Exclude???)
    private val physicRenderer: Box2DDebugRenderer,
    private val physicWorld: World,
    private val camera: Camera,
    stage: Stage
) : IntervalSystem() {
    private val profiler = GLProfiler(Gdx.graphics)

    init {
        if (enabled) {
            stage.isDebugAll = true
            profiler.enable()
        }
    }

    override fun onTick() {
        Gdx.graphics.setTitle("Dinoleon - FPS:${Gdx.graphics.framesPerSecond}, DrawCalls:${profiler.drawCalls}, Binds:${profiler.textureBindings}")
        physicRenderer.render(physicWorld, camera.combined)
        profiler.reset()
    }
}
