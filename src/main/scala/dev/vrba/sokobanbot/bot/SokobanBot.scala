package dev.vrba.sokobanbot.bot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

class SokobanBot(private val token: String) {
  private val client = JDABuilder.createDefault(token)
    .setActivity(Activity.playing("Sokoban with Discord users"))
    .build()

  def run(): Unit = {
    client
  }
}
