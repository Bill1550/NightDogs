package com.loneoaktech.tests.nightdogs

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.loneoaktech.tests.nightdogs.data.model.PetType
import com.loneoaktech.tests.nightdogs.data.model.RiseAndSet
import com.loneoaktech.tests.nightdogs.data.repo.AstronomicalRepo
import com.loneoaktech.tests.nightdogs.data.repo.LocationRepo
import com.loneoaktech.tests.nightdogs.data.repo.PetPixRepo
import com.loneoaktech.tests.nightdogs.support.BaseFragment
import com.loneoaktech.util.toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_pix.*
import kotlinx.android.synthetic.main.fragment_pet_pix.view.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by BillH on 3/2/2019
 */
class PetPixFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {


    companion object {
        val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        const val RC_LOCATION_PERMISSION = 42
    }

    @Inject lateinit var astronomicalRepo: AstronomicalRepo
    @Inject lateinit var locationRepo: LocationRepo
    @Inject lateinit var petPixRepo: PetPixRepo
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NightDogsApplication.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_pix, container, false).apply {
                progressSpinner.visibility = View.INVISIBLE
                refreshButton.setOnClickListener {
                    loadData()
                }
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

                @Suppress("ReplaceSingleLineLet") // OK, i kind of like having the physical order of the calls match the flow.
                locationRepo.getCurrentLocation().let { loc ->
                    astronomicalRepo.getSunTimes(loc).let { times ->
                        displayTimes(loc, times)

                        val pixUrl = petPixRepo.getRandomPetPixUrl( determinePetType(times) )
                        Timber.i("Pet pix url: $pixUrl")
                        picasso
                            .load(pixUrl)
                            .error(R.drawable.ic_autorenew_white_24dp)
                            .into(petImage, object: Callback {
                                override fun onSuccess() {
                                }

                                override fun onError(e: Exception?) {
                                    Timber.e(" Error loading image $pixUrl, error=${e?.message}")
                                }

                            })


                    }


                }
            } catch ( ce: CancellationException ) {
                // fragment has been destroyed,

            } catch ( t: Throwable ) {
                Timber.e(t, "error returned from when refreshing pix fragment")
                context.toast( "Error from api: ${t.message}")
            } finally {
                showProgressSpinner(false)
            }
        }
    }

    private fun showProgressSpinner( show: Boolean ){
        view?.progressSpinner?.visibility = if (show) View.VISIBLE else View.INVISIBLE
        view?.refreshButton?.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun displayTimes( loc: Location, times: RiseAndSet ) {
        Timber.i("Display times: $times")
        view?.apply {
            sunriseView.text = times.riseTime.formatTime()
            sunsetView.text = times.setTime.formatTime()
            locationView.text = "${loc.latitude}, ${loc.longitude}"
        }
    }

    /**
     * Nicely format the time in the local time zone.
     */
    private fun ZonedDateTime.formatTime() =
        this.withZoneSameInstant(ZoneId.systemDefault())
            .format( timeFormatter )

    private val timeFormatter by lazy { DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault()) }

    private fun determinePetType( times: RiseAndSet ): PetType {

        return PetType.values()[Math.abs(Random.nextInt())%PetType.values().size] // TODO actually implement TOD test.
    }

    //-- Handle getting user's permission

    // TODO move this into base class.

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
            context.toast("Returned from set permissions", Toast.LENGTH_LONG)
    }



}