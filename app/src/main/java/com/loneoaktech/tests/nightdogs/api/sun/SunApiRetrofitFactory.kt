package com.loneoaktech.tests.nightdogs.api.sun

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by BillH on 3/3/2019
 */

object SunApiRetrofitFactory {

    fun create(moshi: Moshi): SunRiseSetApi {
        return Retrofit.Builder()
            .baseUrl("https://api.sunrise-sunset.org")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(SunRiseSetApi::class.java)
    }
}