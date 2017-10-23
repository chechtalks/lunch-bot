package org.chechtalks.lunchbot.slack.methods

import com.github.seratch.jslack.Slack
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class MethodsApiConfiguration(private val env: Environment) {

    @Bean
    fun slackMethodsApi() = Slack.getInstance().methods()!!

    @Bean
    fun slackToken() = env.getProperty("slackBotToken")
}