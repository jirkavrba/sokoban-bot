package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.bot.listeners.{CommandListener, ReactionsListener}
import dev.vrba.sokobanbot.game.util.Direction
import net.dv8tion.jda.api.{JDA, JDABuilder}
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.collection.mutable

class SokobanBot(private val token: String) extends ListenerAdapter {

  val client: JDA = JDABuilder
    .createDefault(token)
    .setActivity(Activity.playing("sokoban with Discord users"))
    .build()

  val gamesManager = new GamesManager()

  val levelLoader = new LevelLoader()

  val reactions: mutable.Map[String, Direction] = mutable.LinkedHashMap(
    "⬅️" -> dev.vrba.sokobanbot.game.util.Left,
    "⬆️" -> dev.vrba.sokobanbot.game.util.Up,
    "⬇️" -> dev.vrba.sokobanbot.game.util.Down,
    "➡️" -> dev.vrba.sokobanbot.game.util.Right,
  )

  def start(): Unit = {
    client.addEventListener(new CommandListener(this))
    client.addEventListener(new ReactionsListener(this))
  }
}
