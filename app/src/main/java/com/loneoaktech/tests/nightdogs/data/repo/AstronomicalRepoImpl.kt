package com.loneoaktech.tests.nightdogs.data.repo

import android.app.Application
import android.location.Location
import com.loneoaktech.tests.nightdogs.R
import com.loneoaktech.tests.nightdogs.api.sun.SunRiseSetApi
import com.loneoaktech.tests.nightdogs.data.errors.throwApplicatoinError
import com.loneoaktech.tests.nightdogs.data.model.RiseAndSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by BillH on 3/2/2019
 */
@Singleton
class AstronomicalRepoImpl @Inject constructor(
    val sunApi: SunRiseSetApi,
    val application: Application
) : AstronomicalRepo  {

    /**
     * Read the sunrise/set from the api.
     */
    override suspend fun getSunTimes(location: Location): RiseAndSet {

        // TODO cache results based on date and location.

        return withContext(Dispatchers.IO) {
            sunApi.getSunRiseAndSet(
                latitude = location.latitude,
                longitude = location.longitude
            ).await().let { resp ->
                Timber.i(" Sun times response: $resp")

                // TODO check status
                if (resp.status != "OK")
                    application.throwApplicatoinError("Bad status returned by api: ${resp.status}", R.string.error_sun_time_api_error)

                RiseAndSet(
                    body = "Sun",
                    date = LocalDate.now(),
                    riseTime = resp.results.sunrise,
                    setTime = resp.results.sunset
                )
            }
        }
    }

}