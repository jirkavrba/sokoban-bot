package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.game.level.{Air, Level, Target, Wall}

object LevelRenderer {
  private val emojis = Map(
    "wall" -> "\uD83D\uDFEB",
    "player" -> "\uD83D\uDC77",
    "box" -> "\uD83D\uDCE6",
    "air" -> "⬛",
    "finished_target" -> "\uD83C\uDF1F",
    "target" -> "⭐"
  )

  def render(level: Level): String = {
    level.tiles
      .map(row => row.map(
        tile => {
          tile.material match {
            case Wall   => emojis("wall")

            case Air    => if (level.player == tile.location) emojis("player")
                           else if (level.boxes.contains(tile.location)) emojis("box")
                           else emojis("air")

            case Target => if (level.boxes.contains(tile.location)) emojis("finished_target")
                           else emojis("target")
          }
        }
      ).mkString
    ).mkString("\n")
  }
}
