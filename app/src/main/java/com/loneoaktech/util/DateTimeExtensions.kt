package com.loneoaktech.util

import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

/**
 * Created by BillH on 3/4/2019
 */

/**
 * Nicely format the time in the local time zone.
 */
fun ZonedDateTime.formatTimeMedium() =
    this.withZoneSameInstant(ZoneId.systemDefault())
        .format( timeFormatter )

private val timeFormatter by lazy { DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault()) }
