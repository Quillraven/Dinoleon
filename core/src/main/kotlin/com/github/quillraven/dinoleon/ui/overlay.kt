package com.github.quillraven.dinoleon.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Value.percentHeight
import com.github.quillraven.dinoleon.event.ChangeDifficultyEvent
import com.github.quillraven.dinoleon.event.GameRestartEvent
import com.github.quillraven.dinoleon.event.GameReturnMenuEvent
import com.github.quillraven.dinoleon.screen.GameScreen.Companion.Difficulty.*
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

fun Stage.clearActors() {
    this.unfocusAll()
    this.root.clearActions()
    this.root.clearChildren(true)
}

fun Stage.setMenuOverlay() {
    val skin = Scene2DSkin.defaultSkin
    val theStage = this
    theStage.clearActors()

    // logo
    var logoY: Float
    theStage.addActor(Image(skin.drawable(Drawables.LOGO)).apply {
        this.scaleBy(0.75f)
        this.centerPosition(
            theStage.width * (this.scaleX - 1),
            0f
        )
        logoY = theStage.height - this.height * this.scaleY * 1.1f
        this.y = logoY
    })

    // menu background because scene2d is annoying the sh&% out of me
    // again and setting this as a table background simply doesn't work
    // --> only UI gurus will understand why
    theStage.addActor(Image(skin.drawable(Drawables.TABLE_BG)).apply {
        this.scaleBy(-0.5f)
        this.centerPosition(
            theStage.width * (this.scaleX + 1),
            0f,
        )
        this.y = logoY - this.height * this.scaleY * 1.1f
    })

    // label backgrounds ... same reason as above ... *sad face*
    val headerOffsetX = 500f
    val headerOffsetY = 245f
    val headerOffsetBetween = 120f
    theStage.addActor(Image(skin.drawable(Drawables.HEADER_BG)).apply {
        this.scaleBy(-0.7f)
        this.x = headerOffsetX
        this.y = logoY - headerOffsetY
    })
    theStage.addActor(Image(skin.drawable(Drawables.HEADER_BG)).apply {
        this.scaleBy(-0.7f)
        this.x = headerOffsetX
        this.y = logoY - headerOffsetY - headerOffsetBetween
    })
    theStage.addActor(Image(skin.drawable(Drawables.HEADER_BG)).apply {
        this.scaleBy(-0.7f)
        this.x = headerOffsetX
        this.y = logoY - headerOffsetY - 2 * headerOffsetBetween
    })

    theStage.addActor(
        scene2d.table {
            setFillParent(true)
            defaults().padTop(percentHeight(0.05f, this))

            label("[#00FF00]EASY[]") {
                this.onClick { this.fire(ChangeDifficultyEvent(EASY)) }
                it.row()
            }
            label("[#FFFF00]MEDIUM[]") {
                this.onClick { this.fire(ChangeDifficultyEvent(MEDIUM)) }
                it.row()
            }
            label("[#ff341c]HARD[]") {
                this.onClick { this.fire(ChangeDifficultyEvent(HARD)) }
                it.row()
            }

            bottom().padBottom(percentHeight(0.12f, this))
            pack()
        }
    )
}

fun Stage.setGameOverlay() {
    val skin = Scene2DSkin.defaultSkin
    val theStage = this
    theStage.clearActors()

    val offsetX = 75f
    val offsetY = 10f
    theStage.addActor(Image(skin.drawable(Drawables.TABLE2)).apply {
        this.setPosition(offsetX, offsetY)
    })
    repeat(5) { idx ->
        theStage.addActor(Image(skin.drawable(Drawables.HEART)).apply {
            this.scaleBy(-0.7f)
            this.setPosition(offsetX + 10f + 72 * idx, offsetY + 10f)
            this.userObject = "HEART-$idx"
        })
    }
}

fun Stage.setActiveHearts(hearts: Int) {
    val theStage = this
    repeat(5 - hearts.coerceIn(0, 5)) { idx ->
        theStage.actors.firstOrNull { it.userObject == "HEART-${4 - idx}" }?.let { heartImg ->
            heartImg.clearActions()
            heartImg += Actions.color(Color.DARK_GRAY, 1f, Interpolation.bounceOut)
        }
    }
}

private fun starDrawable(remainingLife: Int): Drawables {
    return when {
        remainingLife >= 4 -> Drawables.STAR1
        remainingLife >= 2 -> Drawables.STAR2
        remainingLife >= 1 -> Drawables.STAR3
        else -> Drawables.STAR4
    }
}

fun Stage.setScoreOverlay(remainingLife: Int) {
    val skin = Scene2DSkin.defaultSkin
    val theStage = this
    theStage.clearActors()

    theStage.addActor(Image(skin.drawable(Drawables.TABLE)).apply {
        this.scaleBy(-0.5f)
        this.centerPosition(
            theStage.width * (this.scaleX + 1),
            theStage.height * (this.scaleX + 1),
        )
    })

    theStage.addActor(Image(skin.drawable(starDrawable(remainingLife))).apply {
        this.scaleBy(-0.25f)
        this.setPosition(485f, 200f)
        this += Actions.color(Color.DARK_GRAY) + Actions.color(Color.WHITE, 1f, Interpolation.swingIn)
    })

    theStage.addActor(Image(skin.drawable(Drawables.HEADER_BG)).apply {
        this.scaleBy(-0.6f)
        this.setPosition(495f, 430f)
    })
    val title = if (remainingLife > 0) "[#00FF00]VICTORY[]" else "[#ff341c]  DEFEAT[]"
    theStage.addActor(Label(title, skin, "huge").apply {
        this.setPosition(560f, 475f)
    })

    theStage.addActor(Image(skin.drawable(Drawables.CLOSE)).apply {
        this.scaleBy(-0.5f)
        this.setPosition(950f, 500f)
        this.onClick { this.fire(GameReturnMenuEvent()) }
    })

    theStage.addActor(Image(skin.drawable(Drawables.RESTART)).apply {
        this.scaleBy(-0.5f)
        this.setPosition(400f, 500f)
        this.onClick { this.fire(GameRestartEvent()) }
    })
}
