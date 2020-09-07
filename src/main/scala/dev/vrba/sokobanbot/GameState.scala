package dev.vrba.sokobanbot

sealed trait GameState

case object NotInGame extends GameState
case object Playing   extends GameState
case object Won       extends GameState
