package com.github.quillraven.dinoleon.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.dinoleon.component.DinoColor
import com.github.quillraven.dinoleon.component.DinoColorComponent
import com.github.quillraven.dinoleon.component.ImageComponent
import com.github.quillraven.dinoleon.component.PhysicComponent
import com.github.quillraven.dinoleon.component.PhysicComponent.Companion.physicCmpFromImage
import com.github.quillraven.dinoleon.event.StartSpawnEvent
import com.github.quillraven.dinoleon.screen.GameScreen.Companion.Difficulty
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.box
import ktx.log.logger

private data class Spawn(val numSpawns: Int, val interval: Float, val speed: Float)

class SpawnSystem(
    private val physicWorld: World = inject(),
    private val atlas: TextureAtlas = inject(),
    private val stage: Stage = inject()
) : IntervalSystem(enabled = false) {
    private lateinit var spawns: Array<Spawn>
    private var spawnIdx = 0
    private var numSpawns = 0
    private var spawnInterval = 0f
    private val wallRegions = Array(DinoColor.size) {
        TextureRegionDrawable(atlas.findRegion("wall_${DinoColor.byOrdinal(it)}"))
    }
    private val currentSpawn: Spawn
        get() = spawns[spawnIdx]

    init {
        changeDifficulty(Difficulty.MEDIUM)
    }

    fun changeDifficulty(difficulty: Difficulty) {
        spawns = SPAWNS[difficulty] ?: throw GdxRuntimeException("No spawns defined for difficulty $difficulty")
        spawnIdx = 0
        val firstSpawn = currentSpawn
        numSpawns = firstSpawn.numSpawns
        spawnInterval = firstSpawn.interval

        stage.root.fire(StartSpawnEvent(spawns.sumOf { it.numSpawns }))
    }

    override fun onTick() {
        if (spawnInterval <= 0f) {
            var spawn = currentSpawn
            spawnInterval = spawn.interval

            world.entity {
                val colorIdx = MathUtils.random(0, DinoColor.size - 1)
                it += DinoColorComponent(color = DinoColor.byOrdinal(colorIdx))

                it += ImageComponent().apply {
                    image = Image().apply {
                        setScaling(Scaling.stretch)
                        drawable = wallRegions[colorIdx]
                        setPosition(15.75f, 1.2f)
                        setSize(0.25f, 1.75f)
                    }
                    layer = 1
                }
                it += physicCmpFromImage(physicWorld, it[ImageComponent].image) { width, height ->
                    box(width, height) { isSensor = true }

                }
                it[PhysicComponent].impulse.set(spawn.speed, 0f)
            }

            numSpawns--
            if (numSpawns <= 0) {
                ++spawnIdx
                if (spawnIdx >= spawns.size) {
                    // everything spawned -> stop system
                    LOG.debug { "Survived all -> victory" }
                    enabled = false
                    return
                }

                LOG.debug { "Survived interval -> go next" }
                spawn = currentSpawn
                numSpawns = spawn.numSpawns
                spawnInterval = spawn.interval
            }
        } else {
            spawnInterval -= deltaTime
        }
    }

    companion object {
        private val SPAWNS = mapOf(
            Difficulty.EASY to arrayOf(
                Spawn(5, 1.5f, -5f),
                Spawn(5, 1.25f, -5f),
                Spawn(5, 0.9f, -5f),
            ),
            Difficulty.MEDIUM to arrayOf(
                Spawn(5, 1.5f, -5f),
                Spawn(5, 1.25f, -5f),
                Spawn(7, 0.9f, -5f),
                Spawn(7, 0.7f, -5.5f),
                Spawn(8, 0.5f, -5.5f),
                Spawn(10, 0.25f, -5.5f)
            ),
            Difficulty.HARD to arrayOf(
                Spawn(7, 0.7f, -5.5f),
                Spawn(8, 0.6f, -5.5f),
                Spawn(9, 0.5f, -6f),
                Spawn(10, 0.25f, -6f)
            )
        )
        private val LOG = logger<SpawnSystem>()
    }
}
