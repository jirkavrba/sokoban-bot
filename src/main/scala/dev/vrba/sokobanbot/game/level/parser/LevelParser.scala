package dev.vrba.sokobanbot.game.level.parser

import dev.vrba.sokobanbot.game.level._
import dev.vrba.sokobanbot.game.util.Location

import scala.collection.mutable.ArrayBuffer

object LevelParser {
  def parse(source: String): Either[ParserError, Level] = {
    val lines = source.split("\n")

    // If there are no lines or the lines are empty
    if (lines.isEmpty || lines.head.isEmpty)
      return Left(EmptyFile)

    val (width, height) = (lines.head.length, lines.length)
    val tiles = Array.ofDim[Tile](width, height)
    val boxes = ArrayBuffer[Location]()

    var playerLocation: Location = null
    var targets = 0

    for (
      (line, y) <- lines.zipWithIndex;
      (char, x) <- line.trim.zipWithIndex
    ) {
      // If the element is out of the given boundaries
      if (x >= width || y >= height)
        return Left(OutOfBounds)

      val location = Location(x, y)

      char match {
        case '.' => tiles(x)(y) = Tile(location, Air)
        case 'W' => tiles(x)(y) = Tile(location, Wall)
        case 'T' => tiles(x)(y) =
          Tile(location, Target)
          targets += 1
        case 'B' =>
          tiles(x)(y) = Tile(location, Air)
          boxes += location
        // TODO: There must be some beautiful alternative of this ugly shit
        case 'P' => // Player location was specified more than once!
          tiles(x)(y) = Tile(location, Air)
          if (playerLocation != null) return Left(DuplicatedPlayerLocation)
          else playerLocation = location
        // The level shouldn't contain anything elese
        case _ => return Left(UnknownLiteral)
      }
    }

    // If the player location was not specified
    if (playerLocation == null)
      return Left(NoPlayerLocation)

    // If there are no boxes/targets
    if (targets == 0 || boxes.isEmpty)
      return Left(NoTargetsAndBoxes)

    // If there are more or less targets than boxes
    if (targets != boxes.length)
      return Left(MismatchedBoxesAndTargets)

    // Otherwise everything should be fine
    Right(Level(tiles, boxes.toSet, playerLocation))
  }
}
