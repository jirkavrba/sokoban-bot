package dev.vrba.sokobanbot.bot

import java.awt.Color

import dev.vrba.sokobanbot.game.SokobanGame
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.{Member, MessageEmbed}

object SokobanEmbeds {
  private def base = new EmbedBuilder()
    .setColor(new Color(207, 48, 62))

  val help: MessageEmbed = base
    .setTitle("Sokoban game")
    .setDescription("Some interesting text I will write later on")
    .build()

  val alreadyInGame: MessageEmbed = base
    .setTitle("You are already playing a game")
    .setDescription(
      """
        |To view the current game use `@Sokoban game`
        |To cancel the game use `@Sokoban cancel`
        |""".stripMargin)
    .build()

  val error: MessageEmbed = base
    .setTitle("Something unexpected happened")
    .setDescription("Sorry about that.\nPlease file an issue on Github")
    .addField("Github issues", "https://github.com/jirkavrba/sokoban-bot/issues", false)
    .build()

  def game(member: Member, game: SokobanGame): MessageEmbed = base
    .setTitle(member.getEffectiveName + "'s game")
    .setDescription(LevelRenderer.render(game.level.get))
    .setFooter(s"Played ${game.moves} moves so far")
    .build()

}
