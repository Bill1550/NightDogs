package com.loneoaktech.tests.nightdogs

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.loneoaktech.tests.nightdogs.data.model.RiseAndSet
import com.loneoaktech.tests.nightdogs.data.repo.AstronomicalRepo
import com.loneoaktech.tests.nightdogs.support.BaseFragment
import kotlinx.android.synthetic.main.fragment_pet_pix.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by BillH on 3/2/2019
 */
class PetPixFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {


    companion object {
        val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        const val RC_LOCATION_PERMISSION = 42
    }

    @Inject lateinit var astronomicalRepo: AstronomicalRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NightDogsApplication.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_pix, container, false).apply {
                progressSpinner.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissionsAndLoad()
    }


    /**
     * Always check to see if permission has ben granted by the user since they can revoke it at any time
     */
    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    fun checkPermissionsAndLoad() {
        context?.let { ctx ->
            if (EasyPermissions.hasPermissions(ctx, *LOCATION_PERMISSION ))
                loadData()
            else
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.rational_location_permission),
                        RC_LOCATION_PERMISSION,
                        *LOCATION_PERMISSION
                )
                // After permission is granted will call back into this function (due to @AfterPermissionGranted attribute)
        }
    }


    private fun loadData() {

        launch(Dispatchers.Main) {
            try {
                showProgressSpinner(true)
                astronomicalRepo.getSunTimes(
                    Location("TEST").apply {
                        latitude = 41.123
                        longitude = -71.23
                    }
                ).let { times ->
                    displayTimes(times)
                }

            } catch ( t: Throwable ) {
                Timber.e(t, "error returned from when refreshing pix fragment")
            } finally {
                showProgressSpinner(false)
            }
        }
    }

    private fun showProgressSpinner( show: Boolean ){
        view?.progressSpinner?.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun displayTimes( times: RiseAndSet ) {
        Timber.i("Display times: $times")
        view?.apply {
            sunriseView.text = "12:34:56"
            sunsetView.text = "01:23:45"
        }
    }


    //-- Handle getting user's permission

    /**
     * Overridden to pass the request response to the Easy Permissions lib.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.i("Permissions denied=$perms")

        if (EasyPermissions.somePermissionPermanentlyDenied(this, LOCATION_PERMISSION.toList() ))
            AppSettingsDialog.Builder(this).build().show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.i("Permissions granted=$perms")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            context?.let{ ctx ->
                Toast.makeText(ctx, "Returned from set permissions", Toast.LENGTH_LONG).show()
            }
    }

}