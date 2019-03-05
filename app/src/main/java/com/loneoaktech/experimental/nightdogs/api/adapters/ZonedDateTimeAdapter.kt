package com.loneoaktech.experimental.nightdogs.api.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Moshi type adapter for ZonedDateTime
 *
 * Created by BillH on 3/3/2019
 */
@Suppress("unused")
class ZonedDateTimeTypeAdapter {

    @ToJson
    fun toJson( time: ZonedDateTime): String {
        return time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @FromJson
    fun fromJson( str: String): ZonedDateTime {
        return ZonedDateTime.parse(str)
    }

}