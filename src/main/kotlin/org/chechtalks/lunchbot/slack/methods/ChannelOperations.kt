package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.api.methods.MethodsClient
import com.github.seratch.jslack.api.methods.request.channels.ChannelsHistoryRequest
import com.github.seratch.jslack.api.model.Message
import org.springframework.stereotype.Component

@Component
class ChannelOperations(private val slackMethodsApi: MethodsClient, private val slackToken: String) {

    fun getMessageHistory(channel: String): List<Message> {
        val request = ChannelsHistoryRequest.builder()
                .token(slackToken)
                .channel(channel)
                .build()

        val response = slackMethodsApi.channelsHistory(request)

        assert(response.isOk) { "Error calling Slack Methods API" }

        return response.messages
    }

}