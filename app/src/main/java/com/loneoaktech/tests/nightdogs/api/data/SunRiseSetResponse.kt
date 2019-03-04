package com.loneoaktech.tests.nightdogs.api.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime

/**
 * Data as returned by the Sunset and Sunrise times api.
 *
 * Created by BillH on 3/3/2019
 */

@JsonClass(generateAdapter = true)
data class SunRiseSetResponse(
    val results: SunRiseSetResults,
    val status: String
)

/**
 * The results object returned by the end point.
 * @field:Json attributes are use to decouple the API field name from Kotlin class property name.
 *
 * Values that aren't critical are nullable so we don't get an error if they are missing.
 */
@JsonClass(generateAdapter = true)
data class SunRiseSetResults(
    @field:Json(name="sunrise") val sunrise: ZonedDateTime,
    @field:Json(name="sunset") val sunset: ZonedDateTime,
    @field:Json(name="solar_noon") val solarNoon: ZonedDateTime?,
    @field:Json(name="civil_twilight_begin") val civilTwilightBegin: ZonedDateTime?,
    @field:Json(name="civil_twilight_end") val civilTwilightEnd: ZonedDateTime?,
    @field:Json(name="nautical_twilight_begin") val nauticalTwilightBegin: ZonedDateTime?,
    @field:Json(name="nautical_twilight_end") val NauticalTwilightEnd: ZonedDateTime?,
    @field:Json(name="astronomical_twilight_begin") val astronomicalTwilightBegin: ZonedDateTime?,
    @field:Json(name="astronomical_twilight_end") val astronomicalTwilightEnd: ZonedDateTime?
)