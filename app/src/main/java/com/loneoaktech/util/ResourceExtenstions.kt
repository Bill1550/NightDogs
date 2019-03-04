package com.loneoaktech.util

import android.content.res.Resources
import androidx.annotation.StringRes
import java.lang.NumberFormatException

/**
 * Created by BillH on 3/4/2019
 */

/**
 * Gets a milliseconds value from a string resource which is formatted as:
 *   "<int> <units>"
 *
 *   i.e.   "123 msec", "30 sec", "10 min", "3 hr"
 *
 *   Throws either IllegalArgumentException if not correctly formatted
 */
fun Resources.getTimeAsLabeledOrNull(@StringRes id: Int ): Long? {

    try {
        MILLISECONDS_VALUE_REGEX.matchEntire(getString(id))?.let { match ->
            val number = match.groupValues[1].toLong()
            val factor = when (match.groupValues[2]) {
                "hr" -> 3600_000L
                "min" -> 60_000L
                "sec" -> 1000L
                "msec" -> 1L
                else -> throw IllegalArgumentException("Regex failure") // regex options don't match cases above
            }
            return number * factor
        }
            ?: throw IllegalArgumentException("Resource entry not correctly formatted as a time span, resource dashboardId=${getResourceName(id)}")

    } catch ( ex: Resources.NotFoundException ) {
        return null
    } catch ( ne: NumberFormatException) {
        throw IllegalArgumentException("invalid number format")
    }

}

private val MILLISECONDS_VALUE_REGEX = Regex("""^(\d+) *(hr|min|sec|msec)$""")