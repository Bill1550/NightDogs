package com.loneoaktech.util

import android.content.Context
import android.location.LocationManager
import androidx.annotation.StringRes
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.loneoaktech.experimental.nightdogs.R

/**
 * Created by BillH on 3/5/2019
 */

object LocationHelper {

    private val locationManager = SingletonHolder<LocationManager, Context> { ctx ->
        ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager}

    private val googleApiAvailability by lazy { GoogleApiAvailability.getInstance() }


    fun isGpsEnabled(context: Context): Boolean =
        locationManager.getInstance(context).isProviderEnabled(LocationManager.GPS_PROVIDER)

    fun isNetworkEnabled(context: Context): Boolean =
        locationManager.getInstance(context).isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    fun isGooglePlayServicesAvalable(context: Context): Boolean =
            googleApiAvailability.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    /**
     * Ret
     */
    fun getLocationErrorMessage(context: Context): String {

        return context.getString(
            when {
                isGooglePlayServicesAvalable(context).not() -> R.string.error_google_play_services_not_available
                isGpsEnabled(context).not() -> R.string.error_gps_disabled
                else -> R.string.error_unknown_location_error
            }
        )

    }
}