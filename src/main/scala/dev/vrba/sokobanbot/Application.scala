package dev.vrba.sokobanbot

import dev.vrba.sokobanbot.game.level.LevelParser

import scala.io.Source

object Application {
  def main(args: Array[String]): Unit = {
    args.headOption match {
      case Some(token) => // Start the application with the provided token
      case None =>  throw new IllegalArgumentException(
        """| Please run this application with a discord token as the only parameter.
           | > $ java -jar sokoban-bot.jar [discord token]
           |""".stripMargin)
    }
  }
}
