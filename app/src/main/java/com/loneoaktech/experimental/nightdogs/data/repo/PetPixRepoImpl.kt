package com.loneoaktech.experimental.nightdogs.data.repo

import android.net.Uri
import android.widget.ImageView
import com.loneoaktech.experimental.nightdogs.R
import com.loneoaktech.experimental.nightdogs.api.pets.PetPixApi
import com.loneoaktech.experimental.nightdogs.data.model.PetType
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A simple implementation of the PetPixRepo interface.
 * Loads a URL from the appropriate random pic endpoint, and optionally
 * loads it into the image view.
 *
 * Created by BillH on 3/4/2019
 */
@Singleton
class PetPixRepoImpl @Inject constructor(
    private val petPixApi: PetPixApi,
    private val picasso: Picasso

) : PetPixRepo {

    /**
     * Get the URL for a random pet of the specified type.
     */
    override suspend fun getRandomPetPixUrl(petType: PetType): Uri {
        return withContext( Dispatchers.IO ) {
            repeat(10) {
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
            PetType.CAT -> petPixApi.getRandomCatAsync()
            PetType.DOG -> petPixApi.getRandomDogAsync()
            PetType.FOX -> petPixApi.getRandomFoxAsync()
        }.await().pixUrl.let { Uri.parse(it) }          // TODO sanitize the URL.
    }

    /**
     * Load a random pet image directly into an image view.
     * Doing the full load in the Repo would allow for an easier implementation of an improved error
     * handling strategy.
     * Some of the random pix url's return a error (i.e. 504).
     * The repo could capture the error from Picasso, and get a new random URL.
     */
    override suspend fun loadPetPix(petType: PetType, imageView: ImageView) {

        repeat(3){
            loadImage( getRandomPetPixUrl(petType), null, imageView).let { t ->
                when(t) {
                    null -> return  // all done
                    is CancellationException -> return // we have been cancelled
                    else -> { Timber.e("Error loading image: ${t.message}")}   // keep trying
                }
            }
        }

        // weren't able to get a good load in 3 tries, try once more and load default image on failure
        loadImage(
            getRandomPetPixUrl(petType),
            if (petType == PetType.DOG) R.drawable.default_dog else R.drawable.default_cat,
            imageView)
    }

    /**
     * Core image loading routine.
     * Uses Picasso to load into ImageView. Detects when an error occurs
     */
    private suspend fun loadImage( url: Uri, errorResource: Int?, imageView: ImageView ): Throwable? {
        return try {
            withTimeout(5000) { // enforce a basic timeout. Some of the pix sources are slow
                suspendCoroutine<Throwable?> { continuation ->
                    picasso.load(url).apply {
                            errorResource?.let{ error(it) }
                        }
                        .into(
                            imageView,
                            object: Callback {
                                override fun onSuccess() {
                                    continuation.resume(null)
                                }

                                override fun onError(e: Exception?) {
                                    Timber.e(" Error loading image $url, error=${e?.message}")
                                    continuation.resume(e)
                                }
                            }
                        )
                }
            }
        } catch (e: Exception) {
            // just return exception
            picasso.cancelRequest(imageView) // cancel request to avoid leaks.
            e
        }

    }
}