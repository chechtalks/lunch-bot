package org.chechtalks.lunchbot.bot.utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.test.assertNull

class ParameterParserTest {

    private val parser = ParameterParser()

    @Test
    fun it_extracts_correct_number_when_there_is_a_single_parameter() {
        val result1 = parseInt("deep=1")
        assertThat(result1, `is`(1))

        val result2 = parseInt("sarlanga deep=10")
        assertThat(result2, `is`(10))

        val result3 = parseInt("abcdeep=999dsd")
        assertThat(result3, `is`(999))

        val result4 = parseInt("command deep=123.4")
        assertThat(result4, `is`(123))
    }

    @Test
    fun it_extracts_correct_number_when_there_are_multiple_parameters() {
        val result1 = parseInt("deep=1 dep=2")
        assertThat(result1, `is`(1))

        val result2 = parseInt("deep=1 deep=2")
        assertThat(result2, `is`(2))

        val result3 = parseInt("deep=1 deep=2 deep=3")
        assertThat(result3, `is`(3))
    }

    @Test
    fun it_returns_null_when_there_is_no_parameter() {
        val result1 = parseInt("dep=1")
        assertNull(result1)

        val result2 = parseInt("no deep")
        assertNull(result2)

        val result3 = parseInt("deep is 2")
        assertNull(result3)

        val result4 = parseInt("deep=one")
        assertNull(result4)
    }

    private fun parseInt(text: String) = parser.parseInt("deep", text)
}