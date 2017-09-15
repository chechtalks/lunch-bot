package org.chechtalks.lunchbot.bot

import me.ramswaroop.jbot.core.slack.SlackService
import me.ramswaroop.jbot.core.slack.models.User
import org.chechtalks.lunchbot.bot.model.DailyMenus.Companion.isMenu
import org.chechtalks.lunchbot.social.FacebookHelper
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.rule.OutputCapture
import org.springframework.core.env.Environment
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@RunWith(MockitoJUnitRunner::class)
@SpringBootTest
@Ignore("I'll rewrite all tests using Spock")
class LunchBotTest {

    @Mock
    lateinit private var session: WebSocketSession

    @Mock
    lateinit private var slackService: SlackService

    @Mock
    lateinit private var facebook: FacebookHelper

    @Mock
    lateinit private var env: Environment

    @InjectMocks
    lateinit private var bot: LunchBot

    @Rule
    var capture = OutputCapture()

    val SOHO_MENU_NOT_FOUND = "No encontre el menu de hoy para Cocina Soho :thinking_face:"
    val COMER_BIEN_MENU_NOT_FOUND = "Todavia no se buscar el menu de Comer Bien :sweat_smile:"
    val DEFAULT_RESPONSE = "No entendí :neutral_face:"

    @Before
    fun init() {
        val user = User()
        user.name = "LunchBot"
        user.id = "UEADGH12S"

        `when`(slackService.currentUser).thenReturn(user)
        `when`(facebook.getFirstPost("CocinaSoho", ::isMenu)).thenReturn(null)
    }

    @Test
    fun given_an_unrecognized_message_should_reply_with_default_response() {
        val textMessage = mockMessage("unrecognized command")
        bot.handleTextMessage(session, textMessage)
        assertThat(capture.toString(), containsString(DEFAULT_RESPONSE))
    }

    @Test
    fun given_a_cocina_soho_menu_request_should_reply_with_menu_info() {
        val textMessage = mockMessage("cual es el menu de cocina Soho?")
        bot.handleTextMessage(session, textMessage)
        assertThat(capture.toString(), containsString(SOHO_MENU_NOT_FOUND))
    }

    private fun mockMessage(message: String): TextMessage {
        return TextMessage("""
        {
            "type": "message",
            "ts": "1358878749.000002",
            "user": "U023BECGF",
            "text": "<@UEADGH12S>: $message"
        }
        """)
    }
}
