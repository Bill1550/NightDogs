package com.loneoaktech.experimental.nightdogs.api.pets

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**

 * Created by BillH on 3/4/2019
 */
interface PetPixApi {

    @GET("https://random.dog/woof.json")
    fun getRandomDogAsync(): Deferred<RandomDogPixResponse>

    @GET("https://aws.random.cat/meow")
    fun getRandomCatAsync(): Deferred<RandomCatPixResponse>

    @GET("https://randomfox.ca/floof/")
    fun getRandomFoxAsync(): Deferred<RandomFoxPixResponse>

}