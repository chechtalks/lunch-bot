package org.chechtalks.lunchbot.bot.commands.impl

import me.ramswaroop.jbot.core.slack.models.Event
import org.chechtalks.lunchbot.bot.commands.BotCommand
import org.chechtalks.lunchbot.bot.commands.CONTEO
import org.chechtalks.lunchbot.bot.commands.PEDIDOS
import org.chechtalks.lunchbot.bot.messaging.BotResponse
import org.chechtalks.lunchbot.bot.utils.ParameterParser
import org.chechtalks.lunchbot.bot.utils.ReservationsCalculator
import org.chechtalks.lunchbot.config.MessageResolver
import org.chechtalks.lunchbot.extensions.*
import org.chechtalks.lunchbot.slack.methods.ApiMessage
import org.chechtalks.lunchbot.slack.methods.ChannelOperations
import org.chechtalks.lunchbot.slack.methods.ReactionOperations
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class SummarizeOrders(
        private val messages: MessageResolver,
        private val parameterParser: ParameterParser,
        private val channelOperations: ChannelOperations,
        private val reactionOperations: ReactionOperations,
        private val reservationsCalculator: ReservationsCalculator)
    : BotCommand {

    private var deep = 1000
    private lateinit var channel: String

    override fun invoked(event: Event): Boolean {
        val invoked = event.text.contains(CONTEO, PEDIDOS)
        if (invoked) {
            deep = parseDeep(event.text)
            channel = event.channelId
        }
        return invoked
    }

    override fun execute(response: BotResponse) {
        val timeMs = measureTimeMillis {
            response.send("Dame unos segundos :timer_clock:...")
            val orderSummaries = getOrderSummaries()

            if (orderSummaries.isEmpty()) {
                response.send(defaultResponse())
                return
            }

            response.send("Sumario de pedidos: $DOUBLE_JUMP")
            orderSummaries.forEach { response.send(it) }
        }

        response.send("Tardé $timeMs ms con deep=$deep")
    }

    private fun parseDeep(text: String) = parameterParser.parseInt("deep", text) ?: deep

    private fun getOrderSummaries(): List<String> {
        return fetchBotMessages()
                .filter { isAMenu(it) }
                .pmap { toMessageWithReactions(it) }
                .filterNotNull()
                .filter { hasReactions(it) }
                .map { toOrderSummary(it) }
    }

    private fun defaultResponse() = messages.get("lunchbot.response.summary.notfound")

    private fun fetchBotMessages() = channelOperations.fetchBotMessageHistory(channel, deep)

    private fun isAMenu(it: ApiMessage) = it.text.isQuoted()

    private fun hasReactions(it: ApiMessage) = it.reactions != null

    private fun toMessageWithReactions(it: ApiMessage) = reactionOperations.fetchReactions(channel, it)

    private fun toOrderSummary(it: ApiMessage): String {
        val sum = reservationsCalculator.calculateSum(it.reactions)
        val menu = it.text.removeQuotes()

        return "$sum pedidos de $menu"
    }
}