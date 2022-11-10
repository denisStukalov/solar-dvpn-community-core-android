package co.sentinel.dvpn.domain.core.extension

import java.util.*

fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()

fun String.Companion.empty() = ""

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}