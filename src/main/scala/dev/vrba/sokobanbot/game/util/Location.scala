package dev.vrba.sokobanbot.game.util

case class Location(x: Int, y: Int) {
  def + (location: Location): Location =
    Location(x + location.x, y + location.y)

  def + (direction: Direction): Location =
    Location(x + direction.x, y + direction.y)
}
