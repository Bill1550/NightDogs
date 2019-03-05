package com.loneoaktech.experimental.nightdogs.data.repo

import android.location.Location

/**
 * Created by BillH on 3/2/2019
 */
interface LocationRepo {

    suspend fun getCurrentLocation(): Location
}