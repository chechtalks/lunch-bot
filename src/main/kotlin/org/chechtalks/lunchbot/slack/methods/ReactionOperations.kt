package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.api.methods.MethodsClient
import com.github.seratch.jslack.api.methods.request.reactions.ReactionsGetRequest
import org.springframework.stereotype.Component

@Component
class ReactionOperations(
        private val slackMethodsApi: MethodsClient,
        private val slackToken: String) {

    fun fetchReactions(channel: String, message: ApiMessage): ApiMessage? {
        val request = ReactionsGetRequest.builder()
                .token(slackToken)
                .channel(channel)
                .timestamp(message.ts)
                .build()

        val response = slackMethodsApi.reactionsGet(request)

        assert(response.isOk) { "Error calling Slack Methods API" }

        return response.message
    }

}