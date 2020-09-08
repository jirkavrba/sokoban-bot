package dev.vrba.sokobanbot.bot

import java.awt.Color

import dev.vrba.sokobanbot.game.SokobanGame
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.{Member, MessageEmbed}

object SokobanEmbeds {
  private val base = new EmbedBuilder()
    .setColor(new Color(207, 48, 62))

  val help: MessageEmbed = base
    .setTitle("Sokoban game")
    .setDescription("Some interesting text I will write later on")
    .build()

  def game(member: Member, game: SokobanGame): MessageEmbed = base
    .setTitle(member.getNickname + "'s game")
    .setFooter(s"Played ${game.moves} moves so far")
    .build
}
