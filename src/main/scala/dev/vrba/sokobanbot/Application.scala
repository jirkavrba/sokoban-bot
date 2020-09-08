package dev.vrba.sokobanbot

import dev.vrba.sokobanbot.bot.SokobanBot

object Application {
  def main(args: Array[String]): Unit = {
    args.headOption match {
      case Some(token) => new SokobanBot(token).run
      case None =>  throw new IllegalArgumentException(
        """| Please run this application with a discord token as the only parameter.
           | > $ java -jar sokoban-bot.jar [discord token]
           |""".stripMargin)
    }
  }
}
