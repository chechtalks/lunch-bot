package org.chechtalks.lunchbot.slack.methods

import java.time.LocalDate
import java.time.ZoneOffset

class SlackTimestamp(val epoch: Long) {

    companion object {
        fun atStartOfDay(): SlackTimestamp {
            val startOfDayEpoch = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).epochSecond
            return SlackTimestamp(startOfDayEpoch)
        }
    }

    override fun toString() = epoch.toString()
}