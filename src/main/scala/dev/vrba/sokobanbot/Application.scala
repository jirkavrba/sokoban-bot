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

    val level = LevelParser.parse("""|....WWWWW..........
                                     |....W...W..........
                                     |....WB..W..........
                                     |..WWW..BWW.........
                                     |..W..B.B.W.........
                                     |WWW.W.WW.W...WWWWWW
                                     |W...W.WW.WWWWW..TTW
                                     |W.B..B..........TTW
                                     |WWWWW.WWW.WPWW..TTW
                                     |....W.....WWWWWWWWW
                                     |....WWWWWWW........""".stripMargin)

    println(level)
  }
}
