package org.chechtalks.lunchbot.bot.commands.impl

import me.ramswaroop.jbot.core.slack.models.Event
import org.chechtalks.lunchbot.bot.commands.BotCommand
import org.chechtalks.lunchbot.bot.commands.CONTEO
import org.chechtalks.lunchbot.bot.commands.PEDIDOS
import org.chechtalks.lunchbot.bot.messaging.BotResponse
import org.chechtalks.lunchbot.bot.utils.ReactionsCounter
import org.chechtalks.lunchbot.config.MessageResolver
import org.chechtalks.lunchbot.extensions.DOUBLE_JUMP
import org.chechtalks.lunchbot.extensions.contains
import org.chechtalks.lunchbot.extensions.isQuoted
import org.chechtalks.lunchbot.extensions.removeQuotes
import org.chechtalks.lunchbot.slack.methods.ApiMessage
import org.chechtalks.lunchbot.slack.methods.ChannelOperations
import org.springframework.stereotype.Component

@Component
class SummarizeOrders(
        messages: MessageResolver,
        private val channelOperations: ChannelOperations,
        private val reactionsCounter: ReactionsCounter)
    : BotCommand {

    private lateinit var channel: String

    private val titleMessage = messages.get("lunchbot.response.summary.title")
    private val orderMessage = messages.get("lunchbot.response.summary.orderof")
    private val ordersMessage = messages.get("lunchbot.response.summary.ordersof")
    private val notFoundMessage = messages.get("lunchbot.response.summary.notfound")
    private val help = messages.get("lunchbot.response.help.summary")

    override fun invoked(event: Event): Boolean {
        val invoked = event.text.contains(CONTEO, PEDIDOS)
        if (invoked) {
            channel = event.channelId
        }
        return invoked
    }

    override fun execute(response: BotResponse) {
        val orderSummaries = getOrderSummaries()

        if (orderSummaries.isEmpty()) {
            response.send(notFoundMessage)
            return
        }

        response.send("$titleMessage $DOUBLE_JUMP")
        orderSummaries.forEach { response.send(it) }
    }

    override fun help() = help

    private fun getOrderSummaries(): List<String> {
        return fetchBotMessages()
                .filter { isAMenu(it) }
                .filter { hasReactions(it) }
                .map { toOrderSummary(it) }
    }

    private fun fetchBotMessages() = channelOperations.fetchTodayBotMessages(channel)

    private fun isAMenu(it: ApiMessage) = it.text.isQuoted()

    private fun hasReactions(it: ApiMessage) = it.reactions != null

    private fun toOrderSummary(it: ApiMessage): String {
        val sum = reactionsCounter.count(it.reactions)
        val menu = it.text.removeQuotes()
        val phrase = getProperPhrase(sum)

        return "*$sum* $phrase _ $menu _"
    }

    private fun getProperPhrase(sum: Int): String {
        return if (sum == 1)
            orderMessage
        else
            ordersMessage
    }
}