package org.chechtalks.lunchbot.bot.commands.impl

import me.ramswaroop.jbot.core.slack.models.Event
import me.ramswaroop.jbot.core.slack.models.Message
import org.chechtalks.lunchbot.bot.commands.AYUDA
import org.chechtalks.lunchbot.bot.commands.BotCommand
import org.chechtalks.lunchbot.bot.commands.SingleMessageBotCommand
import org.chechtalks.lunchbot.config.MessageResolver
import org.chechtalks.lunchbot.extensions.DOUBLE_JUMP
import org.chechtalks.lunchbot.extensions.contains
import org.springframework.stereotype.Component

@Component
class Help(private val messages: MessageResolver, private val commands: List<BotCommand>) : SingleMessageBotCommand {

    override fun invoked(event: Event) = event.text.contains(AYUDA)

    override fun execute(): Message {
        var message = help() + DOUBLE_JUMP

        commands.map { it.help() }
                .filterNotNull()
                .forEach { message += "- $it$DOUBLE_JUMP" }

        return Message(message)
    }

    override fun help() = messages.get("lunchbot.response.help")
}
