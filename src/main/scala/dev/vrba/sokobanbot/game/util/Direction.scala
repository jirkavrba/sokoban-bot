package dev.vrba.sokobanbot.game.util

abstract case class Direction(x: Int, y: Int)

object Up    extends Direction(-1,  0)
object Left  extends Direction( 0, -1)
object Down  extends Direction( 1,  0)
object Right extends Direction( 0,  1)
