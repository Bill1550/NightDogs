package com.loneoaktech.tests.nightdogs.api.pets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * JSON returned by the random fox endpoint.
 * Created by BillH on 3/4/2019
 */
@JsonClass(generateAdapter = true)
data class RandomFoxPixResponse(
    @field:Json(name="image") override val pixUrl: String
) : PetPixResponse