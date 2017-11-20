package org.chechtalks.lunchbot.slack.methods

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset

class SlackTimestamp(val epoch: Long) {

    companion object {

        var clock = Clock.systemDefaultZone()!!

        fun atStartOfDay(): SlackTimestamp {
            val startOfDayEpoch = LocalDate.now(clock)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
                    .epochSecond

            return SlackTimestamp(startOfDayEpoch)
        }
    }

    override fun toString() = epoch.toString()
}