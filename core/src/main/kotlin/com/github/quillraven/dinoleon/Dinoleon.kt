package com.github.quillraven.dinoleon

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.dinoleon.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Dinoleon : KtxGame<KtxScreen>() {
    private val batch: Batch by lazy { SpriteBatch() }

    override fun create() {
        super.create()

        val gameScreen = GameScreen(batch)
        addScreen(gameScreen)
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }
}
