package com.loneoaktech.tests.nightdogs.data.repo

import android.location.Location
import com.loneoaktech.tests.nightdogs.data.model.RiseAndSet
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by BillH on 3/2/2019
 */
@Singleton
class AstronomicalRepoImpl @Inject constructor() : AstronomicalRepo  {

    override suspend fun getSunTimes(location: Location): RiseAndSet {

        delay(750)

        return RiseAndSet(
            body = "Sun",
            riseTime = LocalTime.of(6,20,0),
            setTime = LocalTime.of(18,0,0),
            date = LocalDate.of(2019, 3, 2)
        )
    }

}