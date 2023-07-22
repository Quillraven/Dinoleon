package com.github.quillraven.dinoleon.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.style.label
import ktx.style.set
import ktx.style.skin

enum class Drawables {
    TABLE_BG, LOGO, HEADER_BG, HEART, TABLE2, TABLE, STAR1, STAR2, STAR3, STAR4, CLOSE, RESTART;

    val assetPath: String
        get() = "${this.name.lowercase()}.png"

    val atlasKey: String
        get() = this.name.lowercase()
}

fun Skin.drawable(value: Drawables): Drawable = this.getDrawable(value.atlasKey)

fun createSkin(): Skin {
    val uiAtlas = TextureAtlas("ui.atlas").apply {
        // atlas textures which do not fit into atlas to atlas
        // for easier access and disposal
        Drawables.entries.forEach {
            if (this.findRegion(it.atlasKey) == null) {
                addRegion(it.atlasKey, TextureRegion(Texture(it.assetPath)))
            }
        }
    }

    return skin(uiAtlas) { newSkin ->
        /**
         * Fonts
         */
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font.ttf"))
        val fontBig = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 72
            borderColor = Color.BLACK
            borderWidth = 2f
        })
        val fontHuge = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 84
            borderColor = Color.BLACK
            borderWidth = 2f
        })
        newSkin["font-big"] = fontBig.apply { data.markupEnabled = true }
        newSkin["font-huge"] = fontHuge.apply { data.markupEnabled = true }
        generator.dispose()

        /**
         * Labels
         */
        label { font = fontBig }
        label("huge") { font = fontHuge }
    }
}
