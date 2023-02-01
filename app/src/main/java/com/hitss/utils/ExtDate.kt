package com.hitss.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * This function returns the current date in the format "yyyy-MM-dd".
 * The date is formatted using a `SimpleDateFormat` object and the US locale.
 *
 * @return the current date as a string in the format "yyyy-MM-dd".
 */
fun getToday(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return sdf.format(Date())
}