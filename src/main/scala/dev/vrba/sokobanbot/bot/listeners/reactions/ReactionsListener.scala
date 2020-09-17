package dev.vrba.sokobanbot.bot.listeners.reactions

import dev.vrba.sokobanbot.bot.SokobanBot
import dev.vrba.sokobanbot.bot.util.SokobanEmbeds
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReactionsListener(bot: SokobanBot) extends ListenerAdapter {

  override def onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent): Unit = {
    if (event.getUser.isBot) return

    val member = event.retrieveMember.complete

    val game = bot.gamesManager.games.get(member.getIdLong)
    val expected = bot.gamesManager.messages.get(member.getIdLong)

    if (game.isEmpty || expected.isEmpty || event.getMessageIdLong != expected.get) return

    val message = event.retrieveMessage.complete

    val reaction = event.getReaction
    val emoji = reaction.getReactionEmote.getEmoji

    val updated = bot.reactions.get(emoji) match {
      case Some(action) => action match {
        case MoveAction(direction) => game.get.applyMove(direction)
        case UndoAction => game.get.undoMove
        case NoAction => game.get
      }
      case None => game.get // Ignore other reactions
    }

    bot.gamesManager.games(member.getIdLong) = updated

    reaction.removeReaction(member.getUser).queue()
    message.editMessage(SokobanEmbeds.game(member, updated)).queue()
  }
}
