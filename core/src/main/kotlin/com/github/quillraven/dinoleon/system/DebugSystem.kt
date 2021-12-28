package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem

class DebugSystem(
    private val physicWorld: World,
    private val camera: Camera,
    stage: Stage
) : IntervalSystem() {
    private val physicRenderer = Box2DDebugRenderer()
    private val profiler = GLProfiler(Gdx.graphics)

    init {
        stage.isDebugAll = true
        profiler.enable()
    }

    override fun onTick() {
        Gdx.graphics.setTitle("Dinoleon - FPS:${Gdx.graphics.framesPerSecond}, DrawCalls:${profiler.drawCalls}, Binds:${profiler.textureBindings}")
        physicRenderer.render(physicWorld, camera.combined)
        profiler.reset()
    }

    override fun onDispose() {
        physicRenderer.dispose()
    }
}
