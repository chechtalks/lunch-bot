package org.chechtalks.lunchbot.slack.methods

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.*

class SlackTimestampTest {

    private val zoneOffset = ZoneOffset.UTC
    private val zoneId = ZoneId.of("UTC")

    @Test
    fun `it can get the timestamp at start of this day (edge case, end of day)`() {
        // given
        val currentTime = LocalDateTime.of(2017, 11, 11, 23, 59, 59)
        SlackTimestamp.clock = Clock.fixed(currentTime.toInstant(zoneOffset), zoneId)

        // when
        val startOfDayTimestamp = SlackTimestamp.atStartOfDay()

        // then
        val startOfDayTime = Instant
                .ofEpochSecond(startOfDayTimestamp.epoch)
                .atOffset(zoneOffset)
                .toLocalDateTime()

        val expected = LocalDateTime.of(2017, 11, 11, 0, 0, 0)

        assertThat(startOfDayTime, `is`(expected))
    }


    @Test
    fun `it can get the timestamp at start of this day (edge case, start of day)`() {
        // given
        val currentTime = LocalDateTime.of(2017, 11, 11, 0, 0, 1)
        SlackTimestamp.clock = Clock.fixed(currentTime.toInstant(zoneOffset), zoneId)

        // when
        val startOfDayTimestamp = SlackTimestamp.atStartOfDay()

        // then
        val startOfDayTime = Instant
                .ofEpochSecond(startOfDayTimestamp.epoch)
                .atOffset(zoneOffset)
                .toLocalDateTime()

        val expected = LocalDateTime.of(2017, 11, 11, 0, 0, 0)

        assertThat(startOfDayTime, `is`(expected))
    }
}