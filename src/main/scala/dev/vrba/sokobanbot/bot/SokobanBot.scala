package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.game.SokobanGame
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.{Activity, Message, MessageEmbed}
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.collection.mutable

class SokobanBot(private val token: String) extends ListenerAdapter {

  private val client = JDABuilder
    .createDefault(token)
    .setActivity(Activity.playing("sokoban with Discord users"))
    .build()

  // User ID -> Game
  private val games = mutable.Map.empty[Long, SokobanGame]

  // User ID -> Message ID
  private val messages = mutable.Map.empty[Long, Long]

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

      // TODO: Implement this
      val embed: MessageEmbed = parts.head match {
        case "start"  => SokobanEmbeds.help
        case "game" => SokobanEmbeds.help
        case "stop" => SokobanEmbeds.help
        case _ => SokobanEmbeds.help
      }

      channel.sendMessage(embed).queue()
    }
  }

  override def onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent): Unit = {
    if (event.getUser.isBot) return

    val expected = messages.getOrElse(event.getUser.getIdLong, 0)
    if (event.getMessageIdLong != expected) return

    event.getReactionEmote.getEmoji match {
      // Match emotes to move/reset etc...
      case _ =>
    }
  }
}
