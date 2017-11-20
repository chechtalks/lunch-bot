package org.chechtalks.lunchbot.bot.utils

import com.github.seratch.jslack.api.model.Reaction
import org.springframework.stereotype.Component

@Component
class OrdersCalculator {

    fun calculateSum(reactions: List<Reaction>): Int {
        return reactions
                .mapNotNull { weightOf[it.name] }
                .sum()
    }

}

val weightOf = mapOf("one" to 1,
        "heavy_plus_sign" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "keycap_ten" to 10)