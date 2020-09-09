package dev.vrba.sokobanbot.bot.listeners

import dev.vrba.sokobanbot.bot.{LevelLoader, SokobanBot}
import dev.vrba.sokobanbot.bot.util.SokobanEmbeds
import dev.vrba.sokobanbot.game.{Playing, SokobanGame}
import dev.vrba.sokobanbot.game.level.parser.LevelParser
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.io.Source

class CommandListener(bot: SokobanBot) extends ListenerAdapter {

  private val levelLoader = new LevelLoader()

  override def onGuildMessageReceived(event: GuildMessageReceivedEvent): Unit = {
    if (event.getAuthor.isBot) return

    val message = event.getMessage
    val channel = event.getChannel

    if (message.isMentioned(bot.client.getSelfUser, Message.MentionType.USER)) {
      val parts = message.getContentDisplay
        .split(" ")
        .filterNot(_.isEmpty)
        .drop(1)

      if (parts.isEmpty) {
        channel.sendMessage(SokobanEmbeds.help).queue()
        return
      }

      val level = parts
        .drop(1)
        .headOption
        .getOrElse("000")

      parts.head match {
        case "start" => startNewGame(event, level)
        case "game" => switchCurrentMessage(event)
        case "levels" => listAvailableLevels(event)
        case "cancel" => SokobanEmbeds.help
        case _ => channel.sendMessage(SokobanEmbeds.help).queue()
      }
    }
  }

  private def startNewGame(event: GuildMessageReceivedEvent, level: String): Unit = {
    val user = event.getGuild.retrieveMember(event.getAuthor).complete()
    val channel = event.getChannel

    if (bot.gamesManager.games.contains(user.getIdLong) &&
        bot.gamesManager.games(user.getIdLong).state == Playing) {
      channel.sendMessage(SokobanEmbeds.alreadyInGame).queue()
      return
    }

    bot.levelLoader.loadLevel(level) match {
      // Everything worked fine
      case Some(level) =>
        val game = SokobanGame(state = Playing, level = Some(level))
        val message = channel.sendMessage(SokobanEmbeds.game(user, game)).complete

        bot.gamesManager.games.addOne(user.getIdLong -> game)
        bot.gamesManager.messages.addOne(user.getIdLong -> message.getIdLong)
        bot.reactions.keys.foreach(message.addReaction(_).queue)

      case None => channel.sendMessage(SokobanEmbeds.levelNotFound).queue()
    }
  }

  private def switchCurrentMessage(event: GuildMessageReceivedEvent): Unit = {
    val user = event.getGuild.retrieveMember(event.getAuthor).complete()
    val channel = event.getChannel

    if (!bot.gamesManager.games.contains(user.getIdLong)) {
      channel.sendMessage(SokobanEmbeds.notInGame).queue()
      return
    }


  }

  private def listAvailableLevels(event: GuildMessageReceivedEvent): Unit = {
    val levels = bot.levelLoader.allLevels
    val embed = if (levels.isEmpty) SokobanEmbeds.error
                else SokobanEmbeds.levelsList(levels)

    event.getChannel.sendMessage(embed).queue()
  }
}
