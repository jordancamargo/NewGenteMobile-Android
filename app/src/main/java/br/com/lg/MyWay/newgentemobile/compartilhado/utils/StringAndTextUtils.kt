package br.com.lg.MyWay.newgentemobile.compartilhado.utils

object StringAndTextUtils {

    fun removeLastIf(text: String?, lastChar: Char): String? {
        if (isNullOrEmpty(text)) {
            return text
        }
        if (text?.last() == lastChar) {
            return text.substring(0, text.length - 1)
        }
        return text
    }

    fun isNullOrEmpty(text: String?): Boolean {
        return text.isNullOrEmpty()
    }

    fun stringToHtmlString(s: String): String {
        val sb = StringBuilder()
        val n = s.length
        for (i in 0 until n) {
            when (val c = s[i]) {
                '<' -> sb.append("&lt;")
                '>' -> sb.append("&gt;")
                '&' -> sb.append("&amp;")
                '"' -> sb.append("&quot;")
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }
}
