package dev.vrba.sokobanbot.game

import dev.vrba.sokobanbot.game.level.Level
import dev.vrba.sokobanbot.game.util.Direction

case class SokobanGame(
  state: GameState = NotInGame,
  level: Option[Level] = None,
  moves: Int = 0,
  lastStates: List[Level] = Nil
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
             else SokobanGame(Playing, Some(updated), moves + 1, (level :: lastStates).take(3)) // Only preserve the last 3 moves
    }

    // If the move is not valid, do nothing
    this
  }

  def undoMove: SokobanGame = {
    // Only handle games that are actively in play
    if (!state.isInstanceOf[Playing.type]) return this

    // This gives me some Haskell vibes, ngl
    lastStates match {
      case Nil => this
      case x::xs => SokobanGame(state, Some(x), moves - 1, xs)
    }
  }
}
