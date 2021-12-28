package com.github.quillraven.dinoleon.screen

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.quillraven.dinoleon.component.AnimationComponent
import com.github.quillraven.dinoleon.component.PhysicComponent
import com.github.quillraven.dinoleon.component.RenderComponent
import com.github.quillraven.dinoleon.component.TransformComponent
import com.github.quillraven.dinoleon.component.TransformComponent.Companion.Layer
import com.github.quillraven.dinoleon.system.AnimationSystem
import com.github.quillraven.dinoleon.system.DebugSystem
import com.github.quillraven.dinoleon.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.createWorld

class GameScreen(
    batch: Batch
) : KtxScreen {
    private val viewport = FitViewport(16f, 9f)
    private val stage = Stage(viewport, batch)
    private val physicWorld = createWorld(gravity = Vector2.Zero)
    private val physicRenderer = Box2DDebugRenderer()
    private val gameAtlas = TextureAtlas("game.atlas")
    private val eWorld = World {
        system<AnimationSystem>()
        system<RenderSystem>()
        system<DebugSystem>()

        inject(stage)
        inject(physicWorld)
        inject(physicRenderer)
        inject<Camera>(viewport.camera)
        inject(gameAtlas)
    }

    override fun show() {
        super.show()
        spawnPlayer(1f, 1f, Layer.OBJECTS, "blue-idle")
        spawnPlayer(1.25f, 1.25f, Layer.FOREGROUND, "blue-run")
        spawnPlayer(0.75f, 0.75f, Layer.BACKGROUND, "blue-hit")
    }

    private fun spawnPlayer(x: Float, y: Float, layer: Layer, animation: String) {
        eWorld.entity {
            val transform = add<TransformComponent> {
                position.set(x, y)
                size.set(0.75f, 1f)
                this.layer = layer
            }
            add<AnimationComponent> {
                nextAnimation = animation
            }
            add<RenderComponent> {
                image.drawable = TextureRegionDrawable(gameAtlas.findRegion("blue-dino_blue-10"))
            }
            add<PhysicComponent> {
                body = physicWorld.body(BodyType.DynamicBody) {
                    position.set(
                        transform.position.x + transform.size.x * 0.5f,
                        transform.position.y + transform.size.y * 0.5f
                    )
                    fixedRotation = true
                    allowSleep = false
                    box(transform.size.x, transform.size.y) {
                        friction = 0f
                        isSensor = false
                    }

                    // TODO give access to internal entity of EntityCreateCfg
                    // userData =
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        super.render(delta)
        eWorld.update(delta)
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
        physicWorld.dispose()
        physicRenderer.dispose()
        gameAtlas.dispose()

        // TODO how to remove body from PhysicComponent
        // body.world.destroyBody(body)
        // body.userData = null
    }
}
