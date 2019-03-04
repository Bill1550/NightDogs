package com.loneoaktech.tests.nightdogs.api.adapters

import com.squareup.moshi.Moshi

/**
 * Create the Moshi instance for the API.
 *
 * Created by BillH on 3/3/2019
 */
object ApiMoshiFactory {

    fun create(): Moshi =
            Moshi.Builder()
                .add(ZonedDateTimeTypeAdapter())
                .build()
}