package dev.vrba.sokobanbot.bot.util

import java.awt.Color

import dev.vrba.sokobanbot.game.{NotInGame, Playing, SokobanGame, Won}
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.{Member, MessageEmbed}

object SokobanEmbeds {
  private val repository = "https://github.com/jirkavrba/sokoban-bot"


  val help: MessageEmbed = new EmbedBuilder()
    .setColor(Color.GRAY)
    .setTitle("Sokoban bot")
    .setDescription(
  """
    | Hi, I am a little fun project that enables playing the sokoban puzzle game
    | in Discord chat.
    |
    | - To view available levels, use **@Sokoban levels**
    | - To start a new game, use **@Sokoban start (level)**
    |   if no level is provided, the first one will be picked automatically
    |""".stripMargin)
    .addField("Source code", repository, false)
    .build()

  val alreadyInGame: MessageEmbed = new EmbedBuilder()
    .setColor(Color.RED)
    .setTitle("You are already playing a game")
    .setDescription(
  """
    |To move your current game here, use `@Sokoban game`
    |To cancel the game use `@Sokoban cancel`
    |""".stripMargin)
    .build()

  val notInGame: MessageEmbed = new EmbedBuilder()
    .setColor(Color.RED)
    .setTitle("You are not playing a game")
    .setDescription(
  """
    | To start a new game use `@Sokoban game (level)`
    | To view a list of levels use `@Sokoban levels`
    |""".stripMargin)
    .build()

  val gameCancelled: MessageEmbed = new EmbedBuilder()
    .setColor(Color.GREEN)
    .setTitle("Game was cancelled")
    .setDescription(
      """
        | To start a new game use `@Sokoban game (level)`
        | To view a list of levels use `@Sokoban levels`
        |""".stripMargin)
    .build()

  val error: MessageEmbed = new EmbedBuilder()
    .setColor(Color.RED)
    .setTitle("Something unexpected happened")
    .setDescription("Sorry about that.\nPlease file an issue on Github")
    .addField("Github issues", repository + "/issues", false)
    .build()

  def game(member: Member, game: SokobanGame): MessageEmbed = new EmbedBuilder()
    .setColor(Color.CYAN)
    .setTitle(member.getEffectiveName + "'s game")
    .addField("Moves", game.moves.toString, false)
    .addField("How to play",
  """
    | Your goal is to move all boxes to the target places (marked as stars)
    | To do a move, use reactions below this message
    |""".stripMargin, false)
    .addField("Source code", repository, false)
    .setDescription(game.state match {
      case Playing => LevelRenderer.render(game.level.get)
      case Won => s"""
                     | Congratulations!
                     | You won the level.
                     | To play another level, use `@Sokoban play [level number]`
                     | Levels can be listed with `@Sokoban levels`
                     |""".stripMargin
      // Well this shouldn't happen at all
      case NotInGame =>
        """
          | This is probably a bug.
          | Please create an issue on Github.
          |""".stripMargin
    })
    .build()

  def levelsList(levels: Array[String]): MessageEmbed = new EmbedBuilder()
    .setColor(Color.LIGHT_GRAY)
    .setTitle("Available levels")
    .setDescription(s"${levels.mkString("**", "**, **", "**")}")
    .build()

  val levelNotFound: MessageEmbed = new EmbedBuilder()
    .setColor(Color.RED)
    .setTitle("Level not found")
    .setDescription("To list all available levels, please use `@Sokoban levels`")
    .build()
}
