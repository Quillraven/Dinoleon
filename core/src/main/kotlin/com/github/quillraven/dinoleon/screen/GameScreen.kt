package com.github.quillraven.dinoleon.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.quillraven.dinoleon.component.PhysicComponent.Companion.PhysicComponentListener
import com.github.quillraven.dinoleon.event.*
import com.github.quillraven.dinoleon.system.*
import com.github.quillraven.dinoleon.ui.setActiveHearts
import com.github.quillraven.dinoleon.ui.setGameOverlay
import com.github.quillraven.dinoleon.ui.setMenuOverlay
import com.github.quillraven.dinoleon.ui.setScoreOverlay
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.box2d.createWorld

class GameScreen(
    batch: Batch,
    properties: ObjectMap<String, String>
) : KtxScreen, EventListener {
    private val gameStage = Stage(FitViewport(16f, 9f), batch)
    private val uiStage = Stage(FitViewport(1360f, 765f))
    private val physicWorld = createWorld(gravity = Vector2.Zero).apply {
        autoClearForces = false
    }
    private val gameAtlas = TextureAtlas("game.atlas")
    private val eWorld = World {
        system<PlayerSpawnSystem>()
        system<DinoColorSystem>()
        system<SpawnSystem>()
        system<PhysicSystem>()
        system<DamageSystem>()
        system<AnimationSystem>()
        system<ScenerySystem>()
        system<RenderSystem>()
        system<DespawnSystem>()
        if (properties["debug"].toBoolean()) {
            inject(gameStage.viewport.camera)
            system<DebugSystem>()
        }

        inject(gameStage)
        inject(physicWorld)
        inject(gameAtlas)

        componentListener<PhysicComponentListener>()
    }.also { physicWorld.setContactListener(it.system<PhysicSystem>()) }
    private val mscMenu = Gdx.audio.newMusic(Gdx.files.internal("Surf Rock Light Loop.ogg")).apply { isLooping = true }
    private val mscGame = Gdx.audio.newMusic(Gdx.files.internal("Surfs Up Dude Loop.ogg")).apply { isLooping = true }
    private val sndHit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"))

    private var remainingLife = 5
    private var numSpawns = 0
    private var lastDifficulty = Difficulty.EASY

    init {
        // spawn player
        spawnPlayer()

        // set event listener and menu overlay
        uiStage.addListener(this)
        gameStage.addListener(this)
        Gdx.input.inputProcessor = InputMultiplexer(uiStage, eWorld.system<DinoColorSystem>())
        switchToMenu()
    }

    private fun spawnPlayer() {
        remainingLife = 5
        eWorld.system<PlayerSpawnSystem>().respawnPlayer = true
    }

    private fun switchToMenu() {
        mscGame.stop()
        mscMenu.play()

        eWorld.system<DinoColorSystem>().enabled = false
        eWorld.system<SpawnSystem>().enabled = false
        eWorld.removeAll()
        spawnPlayer()
        uiStage.setMenuOverlay()
    }

    private fun switchToScore() {
        mscGame.stop()
        mscMenu.play()

        eWorld.system<DinoColorSystem>().enabled = false
        eWorld.system<SpawnSystem>().enabled = false
        eWorld.removeAll()
        uiStage.setScoreOverlay(remainingLife)
        spawnPlayer()
    }

    private fun switchToGame(difficulty: Difficulty) {
        mscMenu.stop()
        mscGame.play()

        lastDifficulty = difficulty
        uiStage.setGameOverlay()
        eWorld.system<SpawnSystem>().changeDifficulty(difficulty)
        eWorld.system<DinoColorSystem>().enabled = true
        eWorld.system<SpawnSystem>().enabled = true
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    private fun reduceSpawnCtr() {
        --numSpawns
        if (numSpawns <= 0) {
            switchToScore()
        }
    }

    private fun updateLife(life: Int) {
        remainingLife = life
        sndHit.play()
        uiStage.setActiveHearts(life)
    }

    private fun showDefeat() {
        switchToScore()
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is ChangeDifficultyEvent -> switchToGame(event.difficulty)
            is StartSpawnEvent -> numSpawns = event.numSpawns
            is SpawnRemovalEvent -> reduceSpawnCtr()
            is DinoDamageEvent -> updateLife(event.life)
            is DinoDeathEvent -> showDefeat()
            is GameRestartEvent -> switchToGame(lastDifficulty)
            is GameReturnMenuEvent -> switchToMenu()
            else -> return false
        }
        return true
    }

    override fun render(delta: Float) {
        super.render(delta)
        eWorld.update(delta)
        uiStage.viewport.apply()
        uiStage.act(delta)
        uiStage.draw()
    }

    override fun dispose() {
        super.dispose()
        eWorld.dispose()
        gameStage.dispose()
        uiStage.dispose()
        physicWorld.dispose()
        gameAtlas.dispose()
        mscMenu.dispose()
        mscGame.dispose()
        sndHit.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 24f

        enum class Difficulty {
            EASY, MEDIUM, HARD
        }
    }
}
