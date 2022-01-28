package com.github.quillraven.dinoleon.event

import com.badlogic.gdx.scenes.scene2d.Event
import com.github.quillraven.dinoleon.screen.GameScreen.Companion.Difficulty

data class ChangeDifficultyEvent(val difficulty: Difficulty) : Event()

data class StartSpawnEvent(val numSpawns: Int) : Event()

class SpawnRemovalEvent : Event()

data class DinoDamageEvent(val life: Int) : Event()

class DinoDeathEvent : Event()

class GameRestartEvent : Event()

class GameReturnMenuEvent : Event()
