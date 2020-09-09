package dev.vrba.sokobanbot.bot

import java.io.File

import dev.vrba.sokobanbot.game.level.Level
import dev.vrba.sokobanbot.game.level.parser.LevelParser

import scala.io.Source

class LevelLoader {
  val levelFiles: Array[_ <: File] = {
    val resource = getClass.getResource("/levels")
    val folder = new File(resource.getPath)

    if (folder.exists && folder.isDirectory)
      folder
        .listFiles
        .filter(_.getName.endsWith(".level"))
    else
      Array()
  }

  def allLevels: Array[String] = levelFiles.map(_.getName.replace(".level", ""))

  def loadLevel(name: String): Option[Level] = {
    try {
      val source = Source.fromResource(s"levels/$name.level")
      return LevelParser.parse(source.mkString("")) match {
        case Right(level) => Some(level)
        case Left(_) => None
      }
    }
    catch {
      case _: Throwable => None
    }
  }
}
