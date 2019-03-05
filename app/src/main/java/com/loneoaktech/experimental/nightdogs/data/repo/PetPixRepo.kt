package com.loneoaktech.experimental.nightdogs.data.repo

import android.net.Uri
import android.widget.ImageView
import com.loneoaktech.experimental.nightdogs.data.model.PetType

/**
 * Repo that returns the random pet picture URL's.
 * Actual image loading is done by Picasso in the View.
 * Created by BillH on 3/4/2019
 */
interface PetPixRepo {

    /**
     * Get the URL for a random pet of the specified type.
     */
    suspend fun getRandomPetPixUrl( petType: PetType): Uri

    /**
     * Load a random pet image directly into an image view.
     */
    suspend fun loadPetPix( petType: PetType, imageView: ImageView)
}
