package com.loneoaktech.tests.nightdogs.api.sun

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by BillH on 3/3/2019
 */
interface SunRiseSetApi {

    @GET("/json")
    fun getSunRiseAndSet(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("formatted") formatted: Int = 0
    ): Deferred<SunRiseSetResponse>


}