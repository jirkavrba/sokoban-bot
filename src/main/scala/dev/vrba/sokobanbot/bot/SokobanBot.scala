package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.game.level.parser.LevelParser
import dev.vrba.sokobanbot.game.{Playing, SokobanGame}
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.{Activity, Member, Message, MessageEmbed, User}
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.collection.mutable
import scala.io.Source

class SokobanBot(private val token: String) extends ListenerAdapter {

  private val client = JDABuilder
    .createDefault(token)
    .setActivity(Activity.playing("sokoban with Discord users"))
    .build()

  // User ID -> Game
  private val games = mutable.Map.empty[Long, SokobanGame]

  // User ID -> Message ID
  private val messages = mutable.Map.empty[Long, Long]

  private val reactions = mutable.LinkedHashMap(
    "⬅️" -> dev.vrba.sokobanbot.game.util.Left,
    "⬆️" -> dev.vrba.sokobanbot.game.util.Up,
    "⬇️" -> dev.vrba.sokobanbot.game.util.Down,
    "➡️" -> dev.vrba.sokobanbot.game.util.Right,
  )

  def run(): Unit = client.addEventListener(this)

  override def onGuildMessageReceived(event: GuildMessageReceivedEvent): Unit = {
    if (event.getAuthor.isBot) return

    val message = event.getMessage
    val channel = event.getChannel

    if (message.isMentioned(client.getSelfUser, Message.MentionType.USER)) {
      val parts = message.getContentDisplay
        .split(" ")
        .drop(1)

      if (parts.isEmpty) {
        channel.sendMessage(SokobanEmbeds.help).queue()
        return
      }

      parts.head match {
        case "start"  => startNewGame(event)
        case "game"   => SokobanEmbeds.help
        case "cancel" => SokobanEmbeds.help
        case _ => SokobanEmbeds.help
      }
    }
  }

  override def onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent): Unit = {
    if (event.getUser.isBot) return

    val member = event.retrieveMember().complete()
    val expected = messages.getOrElse(member.getIdLong, 0)

    if (event.getMessageIdLong != expected) return

    val game = games(member.getIdLong)
    val message = event.retrieveMessage().complete()

    val reaction = event.getReaction
    val emoji = reaction.getReactionEmote.getEmoji

    val updated = reactions.get(emoji) match {
      case Some(direction) => game.applyMove(direction)
      case None => game // Ignore other reactions
    }

    games(member.getIdLong) = updated
    message.editMessage(SokobanEmbeds.game(member, updated)).queue()
    reaction.removeReaction(member.getUser).queue()

  }

  private def startNewGame(event: GuildMessageReceivedEvent): Unit = {
    val user = event.getGuild.retrieveMember(event.getAuthor).complete()
    val channel = event.getChannel

    if (games.contains(user.getIdLong) && games(user.getIdLong).state == Playing) {
      channel.sendMessage(SokobanEmbeds.alreadyInGame).queue()
      return
    }

    // TODO: Implement proper level loading
    val source = Source.fromResource("levels/000.level").mkString("")

    LevelParser.parse(source) match {
      // Everything worked fine
      case Right(level) =>
        val game = SokobanGame(
          state = Playing,
          level = Some(level),
          moves = 0
        )

        val message = channel.sendMessage(SokobanEmbeds.game(user, game)).complete()

        games.addOne(user.getIdLong -> game)
        messages.addOne(user.getIdLong -> message.getIdLong)
        reactions.foreach(reaction => message.addReaction(reaction._1).queue())

      // Parsing failed
      case Left(error) => channel.sendMessage(SokobanEmbeds.error).queue() // TODO: Handle parsing errors
    }
  }
}
