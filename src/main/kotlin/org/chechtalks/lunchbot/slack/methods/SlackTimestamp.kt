package org.chechtalks.lunchbot.slack.methods

import java.time.LocalDate
import java.time.ZoneOffset

object SlackTimestamp {

    fun atStartOfDay() = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).epochSecond
}