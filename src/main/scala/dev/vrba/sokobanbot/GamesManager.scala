package dev.vrba.sokobanbot

import dev.vrba.sokobanbot.game.SokobanGame

import scala.collection.mutable

class GamesManager {
  // Discord ID -> Game
  private val games = mutable.Map.empty[String, SokobanGame]

  // TODO: Allow starting new games, loading games from in-memory
}
