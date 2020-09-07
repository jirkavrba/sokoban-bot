package dev.vrba.sokobanbot.game.level

sealed trait TileMaterial

case object Air    extends TileMaterial
case object Wall   extends TileMaterial
case object Target extends TileMaterial
