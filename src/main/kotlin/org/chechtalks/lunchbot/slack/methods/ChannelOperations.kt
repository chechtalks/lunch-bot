package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.api.methods.MethodsClient
import com.github.seratch.jslack.api.methods.request.channels.ChannelsHistoryRequest
import org.chechtalks.lunchbot.bot.BotUser
import org.springframework.stereotype.Component

@Component
class ChannelOperations(
        private val slackMethodsApi: MethodsClient,
        private val slackToken: String) {

    fun fetchMessageHistory(channel: String, count: Int = 1000, oldest: String = "0"): List<ApiMessage> {
        val request = ChannelsHistoryRequest.builder()
                .token(slackToken)
                .channel(channel)
                .count(count)
                .oldest(oldest)
                .build()

        val response = slackMethodsApi.channelsHistory(request)

        if (!response.isOk)
            throw SlackMethodsApiException("Got this error response calling channels.history: ${response.error}")

        return response.messages
    }

    fun fetchTodayBotMessages(channel: String, count: Int = 1000): List<ApiMessage> {
        val startOfDay = SlackTimestamp.atStartOfDay().toString()

        return fetchMessageHistory(channel, count, startOfDay)
                .filter { it.user == BotUser.id }
    }
}