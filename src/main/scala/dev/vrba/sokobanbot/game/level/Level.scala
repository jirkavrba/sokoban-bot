package dev.vrba.sokobanbot.game.level

import dev.vrba.sokobanbot.game.util.Location

case class Level(
  tiles: Array[Array[Tile]],
  boxes: Array[Location],
  player: Location
)

object Level {
  // Parse level from a string
  def fromString(source: String): Option[Level] = {
    // TODO: Implement level parsing from string
    None
  }
}
