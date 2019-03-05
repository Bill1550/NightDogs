package com.loneoaktech.experimental.nightdogs.data.repo

import android.location.Location
import com.loneoaktech.experimental.nightdogs.data.model.RiseAndSet

/**
 * Created by BillH on 3/1/2019
 */
interface AstronomicalRepo {

    suspend fun getSunTimes( location: Location): RiseAndSet
}