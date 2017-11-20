package org.chechtalks.lunchbot.extensions

import org.chechtalks.lunchbot.bot.commands.Keyword

fun String.contains(vararg words: String, ignoreCase: Boolean = true) = words.none { !this.contains(it, ignoreCase) }

fun String.contains(vararg words: Keyword, ignoreCase: Boolean = true) = words.none { !it.isPresent(this, ignoreCase) }

fun String.dropFirstNonLetters() = this.dropWhile { !it.isLetter() }

fun String.preformatted() = "```$this```"

fun String.quoted() = "$QUOTE_SYMBOL $this"

fun String.isQuoted() = this.startsWith(QUOTE_SYMBOL) or this.startsWith(ESCAPED_QUOTE_SYMBOL)

fun String.removeQuotes() = this.removePrefix(ESCAPED_QUOTE_SYMBOL).trim()

const val DOUBLE_JUMP = "\n\n"
const val QUOTE_SYMBOL = ">"
const val ESCAPED_QUOTE_SYMBOL = "&gt;"