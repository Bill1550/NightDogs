package com.loneoaktech.tests.nightdogs.api.pets

/**
 * * Common interface for all random pet pix responses.
 * They all return a url, but with a different field name.
 *
 * Created by BillH on 3/4/2019
 */
interface PetPixResponse {
    val pixUrl: String
}