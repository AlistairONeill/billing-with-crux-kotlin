@file:Suppress("SpellCheckingInspection")

package billing.util

private const val lowerCase = "abcdefghijklmnopqrstuvwxyz"
private const val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
private const val alphabet = lowerCase + upperCase
private const val numbers = "0123456789"
private const val alphaNumeric = alphabet + numbers

fun randomString(
    charset: String = alphaNumeric,
    length: Int = 10
) = (0 until length)
    .map { charset.random() }
    .joinToString("")
