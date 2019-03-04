package com.loneoaktech.tests.nightdogs.api.pets

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.loneoaktech.tests.nightdogs.api.sun.SunRiseSetApi
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by BillH on 3/4/2019
 */
object PetPixApiFactory {

    fun create(moshi: Moshi): PetPixApi {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")    // not the actual random URL, an alternate dog source used as a default.
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(PetPixApi::class.java)
    }
}