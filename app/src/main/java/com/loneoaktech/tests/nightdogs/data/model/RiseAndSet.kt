package com.loneoaktech.tests.nightdogs.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZonedDateTime

/**
 * Created by BillH on 3/1/2019
 */
@Parcelize
data class RiseAndSet(
    val body: String,
    val date: LocalDate,
    val riseTime: ZonedDateTime,
    val setTime: ZonedDateTime
) : Parcelable