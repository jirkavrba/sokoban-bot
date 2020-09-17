package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.bot.listeners.CommandListener
import dev.vrba.sokobanbot.bot.listeners.reactions.{EmojiAction, MoveAction, ReactionsListener, UndoAction}
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

  // Has to be linked hash map to preserve ordering
  val reactions: mutable.Map[String, EmojiAction] = mutable.LinkedHashMap(
    "⬅️" -> MoveAction(dev.vrba.sokobanbot.game.util.Left),
    "⬆️" -> MoveAction(dev.vrba.sokobanbot.game.util.Up),
    "⬇️" -> MoveAction(dev.vrba.sokobanbot.game.util.Down),
    "➡️" -> MoveAction(dev.vrba.sokobanbot.game.util.Right),
    "↩️" -> UndoAction
  )

  def start(): Unit = {
    client.addEventListener(new CommandListener(this))
    client.addEventListener(new ReactionsListener(this))
  }
}
