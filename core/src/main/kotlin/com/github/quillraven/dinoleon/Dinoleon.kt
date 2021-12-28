package com.github.quillraven.dinoleon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.PropertiesUtils
import com.github.quillraven.dinoleon.screen.GameScreen
import com.github.quillraven.dinoleon.ui.createSkin
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin

class Dinoleon : KtxGame<KtxScreen>() {
    private val batch: Batch by lazy { SpriteBatch() }
    private val gameProperties = ObjectMap<String, String>()

    override fun create() {
        super.create()

        Gdx.files.internal("game.properties").reader().use { PropertiesUtils.load(gameProperties, it) }
        Gdx.app.logLevel = gameProperties["log-level"].toInt()

        Scene2DSkin.defaultSkin = createSkin()

        val gameScreen = GameScreen(batch, gameProperties)
        addScreen(gameScreen)
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        Scene2DSkin.defaultSkin.dispose()
    }
}
