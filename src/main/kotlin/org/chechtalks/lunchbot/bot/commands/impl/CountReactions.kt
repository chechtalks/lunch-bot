package org.chechtalks.lunchbot.bot.commands.impl

import me.ramswaroop.jbot.core.slack.models.Event
import me.ramswaroop.jbot.core.slack.models.Message
import org.chechtalks.lunchbot.bot.commands.CONTEO
import org.chechtalks.lunchbot.bot.commands.MultiMessageBotCommand
import org.chechtalks.lunchbot.bot.commands.PEDIDOS
import org.chechtalks.lunchbot.bot.utils.ParameterParser
import org.chechtalks.lunchbot.extensions.contains
import org.chechtalks.lunchbot.slack.methods.ChannelOperations
import org.chechtalks.lunchbot.slack.methods.ReactionOperations
import org.springframework.stereotype.Component

@Component
class CountReactions(
        private val parameterParser: ParameterParser,
        private val channelOperations: ChannelOperations,
        private val reactionOperations: ReactionOperations)
    : MultiMessageBotCommand {

    private val lunch_channel = "C4GN5ADS9"
    private val bot_user = "U4HA35H2A"
    private var deep = 10

    override fun invoked(event: Event): Boolean {
        val invoked = event.text.contains(CONTEO, PEDIDOS)
        if (invoked) deep = parseDeep(event.text)
        return invoked
    }

    override fun execute(): List<Message> {
        return channelOperations.getMessageHistory(lunch_channel, deep)
                .filter { it.user == bot_user }
                .mapNotNull { reactionOperations.getReactions(lunch_channel, it) }
                .filter { it.reactions != null }
                .map { Message("message ${it.text} has reactions: ${it.reactions}") }
                .also { if (it.isEmpty()) return listOf(Message("empty")) }
    }

    private fun defaultResponse() = listOf(Message("CountReactions.defaultResponse"))

    private fun parseDeep(text: String) = parameterParser.parseInt("deep", text) ?: deep
}