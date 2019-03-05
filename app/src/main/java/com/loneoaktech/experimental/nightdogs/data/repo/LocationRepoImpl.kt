package com.loneoaktech.experimental.nightdogs.data.repo

import android.app.Application
import android.location.Location
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.loneoaktech.util.summary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by BillH on 3/3/2019
 */
@Singleton
class LocationRepoImpl @Inject constructor( private val application: Application) : LocationRepo {

    private val locationClient by lazy { LocationServices.getFusedLocationProviderClient(application) }

    /**
     * Gets the current location from the Fused Location Provider
     */
    override suspend fun getCurrentLocation(): Location {

        return withContext(Dispatchers.IO) {

            // Use a suspendCoroutine to convert the callback style api to a suspending coroutine.
            suspendCoroutine<Location> { continuation ->

                try {
                        Timber.w("play service availability: ${GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(application)}")


                        locationClient.lastLocation.addOnCompleteListener { task ->
                            task.result?.let { continuation.resume( it )}
                                ?: continuation.resumeWithException( Exception("Location data is not available"))
                        }
                } catch (se: SecurityException) {
                    // Explicitly catch a security exception to satisfy Lint.
                    // Permissions are (should be) checked in calling view, so this is can be handled as a simple error.
                    continuation.resumeWithException(se)
                } catch (t: Throwable ) {
                    Timber.w("getCurrentLocation resuming with exception: ${t.summary()}")
                    continuation.resumeWithException( t)
                }

            }
        }

    }


}