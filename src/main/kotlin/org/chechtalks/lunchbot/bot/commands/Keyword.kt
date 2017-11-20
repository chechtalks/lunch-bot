package org.chechtalks.lunchbot.bot.commands

class Keyword(vararg val synonymous: String) {

    fun isPresent(command: String, ignoreCase: Boolean = true) = synonymous.any { command.contains(it, ignoreCase) }

}

val MENU = Keyword("menu", "menú")
val COCINA_SOHO = Keyword("soho", "cocina soho")
val COMER_BIEN = Keyword("comer bien", "comerbien")
val LO_DE_ROSA = Keyword("lo de rosa", "loderosa", "lo de rosita")
val SIN_FORMATEAR = Keyword("sin formatear", "sin_formatear", "sin formato", "sin_formato")
val AYUDA = Keyword("ayuda", "help")
val POSTS = Keyword("posts", "posteos", "mensajes")
val CONTEO = Keyword("conteo", "recuento", "suma", "total", "conta", "contá")
val PEDIDOS = Keyword("pedidos", "ordenes", "órdenes", "menu", "menú")