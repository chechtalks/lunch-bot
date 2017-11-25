package org.chechtalks.lunchbot.bot.utils

import org.chechtalks.lunchbot.constants.MENU_POST_1
import org.hamcrest.CoreMatchers.startsWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertThat
import kotlin.test.expect

class MenuParserSpec : Spek({

    given("a menu parser") {
        val menuParser = MenuParser()

        on("raw text parsing") {
            val result = menuParser.parse(MENU_POST_1)
            it("parses all menus") {
                expect(9) { result.size }
            }
        }

        on("json file parsing") {
            val jsonMenus = "/menu/lo-de-rosa.json"
            val rosaMenus = this.javaClass.getResource(jsonMenus)
            val result = menuParser.parse(rosaMenus)
            it("parses all menus") {
                expect(38) { result.size }
                expect("> Carne") { result[1] }
                assertThat(result[32], startsWith("> Calabresa"))
            }
        }
    }
})