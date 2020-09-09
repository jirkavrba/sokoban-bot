package dev.vrba.sokobanbot.bot.listeners

import dev.vrba.sokobanbot.bot.SokobanBot
import dev.vrba.sokobanbot.bot.util.SokobanEmbeds
import dev.vrba.sokobanbot.game.{Playing, SokobanGame}
import dev.vrba.sokobanbot.game.level.parser.LevelParser
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.io.Source

class CommandListener(bot: SokobanBot) extends ListenerAdapter {

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

      parts.head match {
        case "start" => startNewGame(event)
        case "levels" => listAvailableLevels()
        case "game" => SokobanEmbeds.help
        case "cancel" => SokobanEmbeds.help
        case _ => channel.sendMessage(SokobanEmbeds.help).queue()
      }
    }
  }

  private def startNewGame(event: GuildMessageReceivedEvent): Unit = {
    val user = event.getGuild.retrieveMember(event.getAuthor).complete()
    val channel = event.getChannel

    if (bot.gamesManager.games.contains(user.getIdLong) && bot.gamesManager.games(user.getIdLong).state == Playing) {
      channel.sendMessage(SokobanEmbeds.alreadyInGame).queue()
      return
    }

    // TODO: Implement proper level loading
    val source = Source.fromResource("levels/000.level").mkString("")

    LevelParser.parse(source) match {
      // Everything worked fine
      case Right(level) =>
        val game = SokobanGame(state = Playing, level = Some(level))
        val message = channel.sendMessage(SokobanEmbeds.game(user, game)).complete

        bot.gamesManager.games.addOne(user.getIdLong -> game)
        bot.gamesManager.messages.addOne(user.getIdLong -> message.getIdLong)
        bot.reactions.keys.foreach(message.addReaction(_).queue)

      case Left(error) => channel.sendMessage(SokobanEmbeds.error).queue() // TODO: Handle parsing errors
    }
  }
}
