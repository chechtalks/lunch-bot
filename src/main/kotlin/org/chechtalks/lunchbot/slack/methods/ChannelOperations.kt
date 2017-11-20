package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.api.methods.MethodsClient
import com.github.seratch.jslack.api.methods.request.channels.ChannelsHistoryRequest
import org.chechtalks.lunchbot.bot.BotUser
import org.springframework.stereotype.Component

@Component
class ChannelOperations(
        private val slackMethodsApi: MethodsClient,
        private val slackToken: String) {

    fun fetchMessageHistory(channel: String, count: Int = 100): List<ApiMessage> {
        val request = ChannelsHistoryRequest.builder()
                .token(slackToken)
                .channel(channel)
                .count(count)
                .build()

        val response = slackMethodsApi.channelsHistory(request)

        assert(response.isOk) { "Error calling Slack Methods API" }

        return response.messages
    }

    fun fetchBotMessageHistory(channel: String, count: Int = 100): List<ApiMessage> {
        return fetchMessageHistory(channel, count)
                .filter { it.user == BotUser.id }
    }
}