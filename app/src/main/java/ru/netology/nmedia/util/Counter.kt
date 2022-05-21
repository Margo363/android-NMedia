package ru.netology.nmedia.util

import java.math.RoundingMode
import java.text.DecimalFormat

fun counter(item: Int): String {
    return when (item) {
        in 1000..1099 -> {
            val num = roundOffDecimal(item / 1000.0)
            (num + "K")
        }
        in 1100..9999 -> {
            val num = roundOffDecimal(item / 1000.0)
            (num + "K")
        }
        in 10_000..999_999 -> {
            ((item / 1000).toString() + "K")
        }
        in 1_000_000..1_000_000_000 -> {
            val num = roundOffDecimal(item / 1_000_000.0)
            (num + "M")
        }
        else -> item.toString()
    }
}

fun roundOffDecimal(number: Double): String {
    val decimalFormat = DecimalFormat("#.#")
    decimalFormat.roundingMode = RoundingMode.FLOOR
    return decimalFormat.format(number).toString()
}