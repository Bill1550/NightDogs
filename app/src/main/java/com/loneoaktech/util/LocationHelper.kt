package com.loneoaktech.util

import android.content.Context
import android.location.LocationManager

/**
 * Created by BillH on 3/5/2019
 */

object LocationHelper {

    private val locationManager = SingletonHolder<LocationManager, Context> { ctx ->
        ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager}


    fun isGpsEnabled(context: Context): Boolean =
        locationManager.getInstance(context).isProviderEnabled(LocationManager.GPS_PROVIDER)

    fun isNetworkEnabled(context: Context): Boolean =
        locationManager.getInstance(context).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}