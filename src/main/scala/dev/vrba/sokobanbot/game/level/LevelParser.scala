package dev.vrba.sokobanbot.game.level

import dev.vrba.sokobanbot.game.util.Location

import scala.collection.mutable.ArrayBuffer

object LevelParser {
  def parse(source: String): Option[Level] = {
    val lines = source.split("\n")

    // If there are no lines or the lines are empty
    if (lines.isEmpty || lines.head.isEmpty)
      return None

    val (width, height) = (lines.head.length, lines.length)
    val tiles = Array.ofDim[Tile](width, height)
    val boxes = ArrayBuffer[Location]()
    var playerLocation: Location = null

    for (
      (line, y) <- lines.zipWithIndex;
      (char, x) <- line.trim.zipWithIndex
    ) {
      // If the element is out of the given boundaries
      if (x >= width || y >= height)
        return None

      val location = Location(x, y)

      char match {
        case '.' => tiles(x)(y) = Tile(location, Air)
        case 'W' => tiles(x)(y) = Tile(location, Wall)
        case 'T' => tiles(x)(y) = Tile(location, Target)
        case 'B' => boxes += location
        // TODO: There must be some beautiful alternative of this ugly shit
        case 'P' => // Player location was specified more than once!
                    if (playerLocation != null) return None
                    else playerLocation = location
      }
    }

    // TODO: Handle cases with num of targets != num of boxes...
    Some(Level(tiles, boxes.toArray, playerLocation))
  }
}
