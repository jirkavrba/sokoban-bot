package dev.vrba.sokobanbot.bot

import dev.vrba.sokobanbot.game.SokobanGame

import scala.collection.mutable

class GamesManager {
  // These are just type aliases to make the map types more self-explanatory
  type UserId = Long
  type MessageId = Long

  val games = mutable.Map.empty[UserId, SokobanGame]
  val messages = mutable.Map.empty[UserId, MessageId]
}
