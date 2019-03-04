package com.loneoaktech.tests.nightdogs.api.pets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by BillH on 3/4/2019
 */
@JsonClass(generateAdapter = true)
data class RandomDogPixResponse(
    @field:Json(name="url") override val pixUrl: String
) : PetPixResponse