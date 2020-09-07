package dev.vrba.sokobanbot.game

import dev.vrba.sokobanbot._
import dev.vrba.sokobanbot.game.level.{Level, Wall}
import dev.vrba.sokobanbot.game.util.{Direction, Location}

case class SokobanGame(
  state: GameState = NotInGame,
  level: Option[Level] = None,
  moves: Int = 0
) {
  def applyMove(direction: Direction): SokobanGame = {
    // Only handle games that are actively in play
    if (!state.isInstanceOf[Playing.type]) return this

    // Due to state management, this can be safely assured
    assert(this.level.isDefined)

    val level = this.level.get
    val target = level.player + direction

    if (level.isValidPlayerPosition(target) && level.canMoveBox(target, direction)) {
      val updated = level.moveBox(target, direction)
                         .movePlayer(direction)

      return if (updated.isSolved) SokobanGame(Won, Some(updated), moves)
             else SokobanGame(Playing, Some(updated), moves + 1)
    }

    // If the move is not valid, do nothing
    this
  }
}
