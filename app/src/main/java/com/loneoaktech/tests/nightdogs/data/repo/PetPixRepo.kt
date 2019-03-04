package com.loneoaktech.tests.nightdogs.data.repo

import android.net.Uri
import com.loneoaktech.tests.nightdogs.data.model.PetType

/**
 * Created by BillH on 3/4/2019
 */
interface PetPixRepo {

    /**
     * Get the URL for a random pet of the specified type.
     */
    suspend fun getRandomPetPixUrl( petType: PetType): Uri
}
