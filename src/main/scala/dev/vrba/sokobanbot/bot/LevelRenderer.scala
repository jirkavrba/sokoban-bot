package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.game.level.{Air, Level, Target, Wall}

object LevelRenderer {
  def render(level: Level): String = {
    level.tiles
      .map(row => row.map(
        tile => {
          if (level.player == tile.location) "\uD83D\uDE0A"
          else if (level.boxes.contains(tile.location)) "\uD83D\uDCE6"
          else tile.material match {
            case Wall   => "\uD83D\uDFEA"
            case Air    => "⬛"
            case Target => "\uD83C\uDF1F"
          }
        }
      ).mkString
    ).mkString("\n")
  }
}
