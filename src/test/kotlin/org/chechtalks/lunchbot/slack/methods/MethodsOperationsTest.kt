package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.Slack
import com.github.seratch.jslack.api.methods.MethodsClient
import com.github.seratch.jslack.api.methods.request.channels.ChannelsHistoryRequest
import com.github.seratch.jslack.api.methods.request.reactions.ReactionsGetRequest
import com.github.seratch.jslack.api.model.Message
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(MethodsApiConfiguration::class, ChannelOperations::class))
class MethodsOperationsTest {

    private val A_CHANNEL = "C4GN5ADS9"
    private val A_TOKEN = "xoxb-153343187078-vLEFK2YkvXUCaWRIb9wLYZkB"

    @Autowired lateinit var channelOperations: ChannelOperations

    @Test
    @Ignore
    fun it_fetches_historic_messages_from_channel() {
        //given
        val mockEnv = mock<Environment> {
            on { getProperty("slackBotToken") } doReturn A_TOKEN
        }

        val messageHistory = channelOperations.getMessageHistory(A_CHANNEL)

        //then
        assertThat(messageHistory.size, Matchers.greaterThan(50))
    }

    @Test
    @Ignore
    fun it_does_something_about_reactions() {
        //given
        val slackMethodsApi = Slack.getInstance().methods()
        val aMessage = fetchAMessage(slackMethodsApi)

        val request = ReactionsGetRequest.builder()
                .token(A_TOKEN)
                .channel(A_CHANNEL)
                .timestamp(aMessage.ts)
                .build()

        //when
        val response = slackMethodsApi.reactionsGet(request)

        //then
        assertTrue { response.isOk }

        val resultingMessage = response.message
        println(resultingMessage.text)
        println(resultingMessage.reactions)

    }

    private fun fetchAMessage(slackMethodsApi: MethodsClient): Message {
        val request = ChannelsHistoryRequest.builder()
                .token(A_TOKEN)
                .channel(A_CHANNEL)
                .count(1)
                .build()
        val response = slackMethodsApi.channelsHistory(request)

        return response.messages.first()
    }
}