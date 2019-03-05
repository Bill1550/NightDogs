package com.loneoaktech.experimental.nightdogs.data.repo

import android.app.Application
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                        locationClient.lastLocation.addOnCompleteListener { task ->
                            continuation.resume( task.result?: throw Exception("Location not available") ) // TODO nicer error
                        }
                } catch (se: SecurityException) {
                    // Explicitly catch a security exception to satisfy Lint.
                    // Permissions are (should be) checked in calling view, so this is can be handled as a simple error.
                    continuation.resumeWithException(se)
                } catch (t: Throwable ) {
                    continuation.resumeWithException( t)
                }

            }
        }

    }


}