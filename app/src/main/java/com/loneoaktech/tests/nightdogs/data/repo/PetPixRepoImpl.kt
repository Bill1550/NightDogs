package com.loneoaktech.tests.nightdogs.data.repo

import android.app.Application
import android.net.Uri
import com.loneoaktech.tests.nightdogs.api.pets.PetPixApi
import com.loneoaktech.tests.nightdogs.data.errors.GeneralApplicationError
import com.loneoaktech.tests.nightdogs.data.model.PetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by BillH on 3/4/2019
 */
@Singleton
class PetPixRepoImpl @Inject constructor(
    private val petPixApi: PetPixApi
) : PetPixRepo {

    override suspend fun getRandomPetPixUrl(petType: PetType): Uri {
        return withContext( Dispatchers.IO ) {
            for( i in 0..10) {
                loadUrl(petType).run {
                    Timber.i("----- url=$this")
                    if (lastPathSegment?.endsWith("mp4") != true)
                        return@withContext this
                    Timber.w("Rejecting pix url: $this")
                }
            }
            return@withContext Uri.EMPTY
        }
    }

    private suspend fun loadUrl( petType: PetType): Uri {
        return when (petType) {
            PetType.CAT -> petPixApi.getRandomCat()
            PetType.DOG -> petPixApi.getRandomDog()
            PetType.FOX -> petPixApi.getRandomFox()
        }.await().pixUrl.let { Uri.parse(it) }          // TODO sanitize the URL.
    }

}