package co.sentinel.cosmos.core.util

fun areValidKeywords(string: String): Boolean {
    return string.matches("^[a-zA-Z\\s+]*$".toRegex())
}