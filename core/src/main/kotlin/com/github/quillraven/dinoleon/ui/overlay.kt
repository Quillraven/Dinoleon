package com.github.quillraven.dinoleon.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Value.percentHeight
import com.github.quillraven.dinoleon.event.ChangeDifficultyEvent
import com.github.quillraven.dinoleon.screen.GameScreen.Companion.Difficulty.*
import ktx.actors.centerPosition
import ktx.actors.onClick
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
        })
    }
}
