package com.loneoaktech.tests.nightdogs.api.pets

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

/**

 * Created by BillH on 3/4/2019
 */
interface PetPixApi {

    @GET("https://random.dog/woof.json")
    fun getRandomDog(): Deferred<RandomDogPixResponse>

    @GET("https://aws.random.cat/meow")
    fun getRandomCat(): Deferred<RandomCatPixResponse>

    @GET("https://randomfox.ca/floof/")
    fun getRandomFox(): Deferred<RandomFoxPixResponse>

}