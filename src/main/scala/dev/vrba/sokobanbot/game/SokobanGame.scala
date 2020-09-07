package dev.vrba.sokobanbot.game

import dev.vrba.sokobanbot.game.level.Level

case class SokobanGame (
  level: Level,
  moves: Int = 0
)
