package org.chechtalks.lunchbot.slack.methods

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class SlackTimestampTest {

    @Test
    fun atStartOfDayTest() {
        // given
        val startOfDayTs = SlackTimestamp.atStartOfDay()

        // when
        val startOfDayInstant = Instant.ofEpochSecond(startOfDayTs.epoch)
        val startOfDayZoned = startOfDayInstant.atOffset(ZoneOffset.UTC)
        val localDateTime = startOfDayZoned.toLocalDateTime()

        // then
        assertThat(localDateTime, `is`(LocalDate.now().atStartOfDay()))
    }
}