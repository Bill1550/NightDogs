package com.loneoaktech.experimental.nightdogs.data.repo

import android.app.Application
import android.location.Location
import com.loneoaktech.experimental.nightdogs.R
import com.loneoaktech.experimental.nightdogs.api.sun.SunRiseSetApi
import com.loneoaktech.experimental.nightdogs.data.errors.throwApplicationError
import com.loneoaktech.experimental.nightdogs.data.model.RiseAndSet
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
    private val sunApi: SunRiseSetApi,
    val application: Application
) : AstronomicalRepo  {

    /**
     * Read the sunrise/set from the api.
     */
    override suspend fun getSunTimes(location: Location): RiseAndSet {

        // TODO cache results based on date and location.

        return withContext(Dispatchers.IO) {
            sunApi.getSunRiseAndSetAsync(
                latitude = location.latitude,
                longitude = location.longitude
            ).await().let { resp ->
                Timber.i(" Sun times response: $resp")

                // TODO check status
                if (resp.status != "OK")
                    application.throwApplicationError("Bad status returned by api: ${resp.status}", R.string.error_sun_time_api_error)

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