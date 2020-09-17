package dev.vrba.sokobanbot.bot.listeners.reactions

import dev.vrba.sokobanbot.game.util.Direction

sealed trait EmojiAction

case object NoAction extends EmojiAction
case object UndoAction extends EmojiAction
case class MoveAction(direction: Direction) extends EmojiAction
