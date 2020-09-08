package dev.vrba.sokobanbot.game.level

import dev.vrba.sokobanbot.game.util.{Direction, Location}

case class Level(
  tiles: Array[Array[Tile]],
  boxes: Array[Location],
  player: Location
) {
  def inBounds(location: Location): Boolean =
    location.x >= 0 &&
    location.y >= 0 &&
    location.x < tiles.length &&
    // Needs to be handled in this manner,
    // as if the level is empty (shouldn't be tho) it would rise OutOfBoundsException
    location.y < tiles.headOption.getOrElse(Array()).length

  def tile(location: Location): Tile =
    if (inBounds(location)) tiles(location.x)(location.y)
    else Tile(Location(0, 0), Wall) // Default tile

  def moveBox(targetTile: Location, direction: Direction): Level =
    this.copy(boxes = boxes.map( box =>
      if (box == targetTile) targetTile + direction
      else box
    ))

  def movePlayer(direction: Direction): Level =
    this.copy(player = player + direction)

  def isValidPlayerPosition(location: Location): Boolean =
    this.inBounds(location) &&
    this.tile(location).material != Wall

  def canMoveBox(from: Location, direction: Direction): Boolean =
    // Either there is no box on the target tile
    !boxes.contains(from) ||
    // Or the box can be pushed in the given direction
    (tile(from + direction).material != Wall && !boxes.contains(from + direction)) // Two stacked boxes cannot be pushed

  def isSolved: Boolean =
    this.tiles
      .flatten
      .filter(_.material == Target)
      .forall(tile => boxes.contains(tile.location))
}

object Level {
  // Parse level from a string
  def fromString(source: String): Option[Level] = {
    // TODO: Implement level parsing from string (move to a separated module?)
    None
  }
}
