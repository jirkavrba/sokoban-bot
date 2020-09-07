package dev.vrba.sokobanbot.game

import dev.vrba.sokobanbot.{GameState, NotInGame}
import dev.vrba.sokobanbot.game.level.Level

case class SokobanGame (
  state: GameState = NotInGame,
  level: Option[Level] = None,
  moves: Int = 0
)
