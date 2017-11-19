package org.chechtalks.lunchbot.bot.utils

import org.springframework.stereotype.Component

@Component
class ParameterParser {

    fun parseInt(parameter: String, message: String): Int? {
        val regex = Regex(""".*$parameter=(\d+).*""", RegexOption.DOT_MATCHES_ALL)

        return regex.firstGroupMatching(message)?.value?.toInt()
    }

}

private fun Regex.firstGroupMatching(message: String) = this.matchEntire(message)?.groups?.get(1)