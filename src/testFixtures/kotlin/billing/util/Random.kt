package billing.util

private val lowerCase = "abcdefghijklmnopqrstuvwxyz"
private val upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
private val alphabet = lowerCase + upperCase
private val numbers = "0123456789"
private val alphaNumeric = alphabet + numbers

fun randomString(
    charset: String = alphaNumeric,
    length: Int = 10
) = (0 until length)
    .map { charset.random() }
    .joinToString("")
