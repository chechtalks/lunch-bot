package org.chechtalks.lunchbot.bot.utils

import com.github.seratch.jslack.api.model.Reaction
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ReactionsCounterTest {

    private val counter = ReactionsCounter()

    @Test
    fun it_counts_singles() {
        //given
        val one = aReaction("one", 1)
        val two = aReaction("two", 1)
        val three = aReaction("three", 1)
        val four = aReaction("four", 1)
        val seven = aReaction("seven", 1)
        val reactions = listOf(one, two, three, four, seven)

        //when
        val result = counter.count(reactions)

        //then
        assertThat(result, `is`(1 + 2 + 3 + 4 + 7))
    }

    @Test
    fun it_counts_multiples() {
        //given
        val one = aReaction("one", 1)
        val plus = aReaction("heavy_plus_sign", 2)
        val five = aReaction("five", 3)
        val eight = aReaction("eight", 2)
        val reactions = listOf(one, plus, five, eight)

        //when
        val result = counter.count(reactions)

        //then
        assertThat(result, `is`(1 + 1 * 2 + 5 * 3 + 8 * 2))
    }

    @Test
    fun it_doesnt_count_invalids() {
        //given
        val nine = aReaction("nine", 1)
        val ten = aReaction("keycap_ten", 1)
        val six = aReaction("six", 3)
        val fake = aReaction("fake", 1)
        val twwwo = aReaction("twwwo", 1)
        val reactions = listOf(nine, ten, six, fake, twwwo)

        //when
        val result = counter.count(reactions)

        //then
        assertThat(result, `is`(9 + 10 + 6 * 3))
    }

    private fun aReaction(name: String, count: Int) = Reaction.builder()
            .name(name)
            .count(count)
            .build()

}