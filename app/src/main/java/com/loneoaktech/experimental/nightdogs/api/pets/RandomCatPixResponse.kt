package com.loneoaktech.experimental.nightdogs.api.pets

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * JSON returned by the random cats endpoint.
 *
 * Created by BillH on 3/4/2019
 */
@JsonClass(generateAdapter = true)
data class RandomCatPixResponse(
    @field:Json(name="file") override val pixUrl: String
) : PetPixResponse