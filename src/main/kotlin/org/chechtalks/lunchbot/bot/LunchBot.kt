package org.chechtalks.lunchbot.bot

import me.ramswaroop.jbot.core.slack.Bot
import me.ramswaroop.jbot.core.slack.Controller
import me.ramswaroop.jbot.core.slack.EventType
import me.ramswaroop.jbot.core.slack.models.Event
import org.chechtalks.lunchbot.bot.commands.BotCommand
import org.chechtalks.lunchbot.bot.messaging.BotResponse
import org.chechtalks.lunchbot.config.MessageResolver
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class LunchBot(private val env: Environment, private val commands: List<BotCommand>, private val messages: MessageResolver) : Bot() {

    private val LOG = LoggerFactory.getLogger(LunchBot::class.java)

    override fun getSlackToken(): String {
        val slackToken = env.getProperty("slackBotToken")
        LOG.trace("getting slack token : $slackToken")

        return slackToken
    }

    override fun getSlackBot(): Bot {
        return this
    }

    @Controller(events = arrayOf(EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE))
    fun onMessage(session: WebSocketSession, event: Event) {

        val response = BotResponse(this, session, event)

        commands.filter { it.invoked(event) }
                .also { handleUnrecognizedCommand(it, response) }
                .forEach { it.execute(response) }
    }

    private fun handleUnrecognizedCommand(it: List<BotCommand>, response: BotResponse) {
        if (it.isEmpty())
            response.send(messages.get("lunchbot.response.default"))
    }
}
