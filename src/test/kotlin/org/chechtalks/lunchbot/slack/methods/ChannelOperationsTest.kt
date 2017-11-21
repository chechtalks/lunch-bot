package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.Slack
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.chechtalks.lunchbot.bot.BotUser
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class ChannelOperationsTest {

    private val channel = "fake-channel"
    private val channelBroken = "broken-channel"
    private val token = "fake-token"
    private val botUser = "fake-user"
    private val zoneOffset = ZoneOffset.UTC
    private val zoneId = ZoneId.of("UTC")
    private val wireMockPort = 9090

    private lateinit var wireMockServer: WireMockServer

    private lateinit var channelOperations: ChannelOperations

    @Before
    fun setup() {
        val config = WireMockConfiguration.wireMockConfig()
                .port(wireMockPort)
                .usingFilesUnderDirectory("src/test/resources/wiremock")
        wireMockServer = WireMockServer(config)
        wireMockServer.start()

        val methodsApi = Slack.getInstance().methods()
        methodsApi.setEndpointUrlPrefix("http://localhost:$wireMockPort/")

        BotUser.id = botUser

        val mockTime = LocalDateTime.of(2017, 11, 20, 12, 0)
        SlackTimestamp.clock = Clock.fixed(mockTime.toInstant(zoneOffset), zoneId)

        channelOperations = ChannelOperations(methodsApi, token)
    }

    @After
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `it fetches historic messages from channel`() {
        //when
        val messages = channelOperations.fetchMessageHistory(channel)

        //then
        assertThat(messages.size, greaterThan(500))

        val messagesWithReactions = messages.filter { it.reactions != null }
        assertThat(messagesWithReactions.size, greaterThan(1))
    }

    @Test
    fun `it fetches only bot messages from this day`() {
        //when
        val messages = channelOperations.fetchTodayBotMessages(channel)

        //then
        assertThat(messages.size, greaterThan(50))
        assertThat(messages.size, lessThan(100))
    }

    @Test
    fun `it fetches reactions in messages`() {
        //when
        val messages = channelOperations.fetchTodayBotMessages(channel)

        //then
        val messagesWithReactions = messages.filter { it.reactions != null }
        assertThat(messagesWithReactions.size, `is`(1))

        val reactionMessage = messagesWithReactions.first()
        assertTrue { reactionMessage.text.contains("Muzzarella y anchoas") }
    }

    @Test
    fun `it handles errors`() {
        assertFailsWith<SlackMethodsApiException> {
            channelOperations.fetchTodayBotMessages(channelBroken)
        }
    }
}