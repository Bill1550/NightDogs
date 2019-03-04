package com.loneoaktech.tests.nightdogs

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loneoaktech.tests.nightdogs.data.model.PetType
import com.loneoaktech.tests.nightdogs.data.model.RiseAndSet
import com.loneoaktech.tests.nightdogs.data.repo.AstronomicalRepo
import com.loneoaktech.tests.nightdogs.data.repo.LocationRepo
import com.loneoaktech.tests.nightdogs.data.repo.PetPixRepo
import com.loneoaktech.tests.nightdogs.support.BaseFragment
import com.loneoaktech.util.formatTimeMedium
import com.loneoaktech.util.getTimeAsLabeledOrNull
import com.loneoaktech.util.launchWithUx
import com.loneoaktech.util.toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_pix.*
import kotlinx.android.synthetic.main.fragment_pet_pix.view.*
import kotlinx.coroutines.*
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment that displays the pet picture.
 * Has the current location and sunrise and sunset times at the top.
 *
 * Provides a refresh button to get a different pet.
 *
 * Created by BillH on 3/2/2019
 */
class PetPixFragment : BaseFragment() {


    companion object {
        val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
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

        // Double check permissions every time we run since user may disable them at any time.
        checkPermissionAndRun(LOCATION_PERMISSION, R.string.rationale_location_permission){
            loadData()
            startAutoRefresh()
        }

    }

    override fun onPause() {
        super.onPause()
        kilAutoRefresh()
    }


    private fun loadData() {

        launchWithUx( ::showProgressSpinner, ::showError ) {
            @Suppress("ReplaceSingleLineLet") // OK, i kind of like having the physical order of the calls match the flow.
            locationRepo.getCurrentLocation().let { loc ->
                astronomicalRepo.getSunTimes(loc).let { times ->
                    bindTimes(loc, times)
                    val pet = determinePetType(times)
                    val pixUrl = petPixRepo.getRandomPetPixUrl( pet )
                    Timber.i("Pet pix url: $pixUrl")
                    picasso
                        .load(pixUrl.toString())
                        .error(if (pet == PetType.DOG) R.drawable.default_dog else R.drawable.default_cat)
                        .into(petImage, object: Callback {
                                override fun onSuccess() {
                                }

                                override fun onError(e: Exception?) {
                                    Timber.e(" Error loading image $pixUrl, error=${e?.message}")
                                }
                            }
                        )
                }
            }
        }
    }

    /**
     * Turn thr progress spinner on and off.
     * Also hide the refresh button while we are loading.
     */
    private fun showProgressSpinner( show: Boolean ){
        view?.progressSpinner?.visibility = if (show) View.VISIBLE else View.INVISIBLE
        view?.refreshButton?.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Display an appropriate error message to the user.
     * A real app would need to do more.
     */
    private fun showError( t: Throwable ){
        Timber.e(t, "error returned from when refreshing pix fragment")
        context.toast( "Error from api: ${t.message}")
    }

    private fun bindTimes(loc: Location, times: RiseAndSet ) {
        Timber.i("Display times: $times")
        view?.apply {
            sunriseView.text = times.riseTime.formatTimeMedium()
            sunsetView.text = times.setTime.formatTimeMedium()
            locationView.text = getString(R.string.location_format, loc.latitude, loc.longitude )
        }
    }


    /**
     * Business rules here. Should really be in a central spot.
     * Determine whether to display a dog or a cat pix
     */
    private fun determinePetType( times: RiseAndSet ): PetType {
        return ZonedDateTime.now().let { now ->
            if ( (now >= times.riseTime) && (now <= times.setTime)  == resources.getBoolean(R.bool.cats_by_day) )
                PetType.CAT
            else
                PetType.DOG
        }
    }


    private var refreshJob: Job? = null

    private fun startAutoRefresh() {
        refreshJob = launch {
            try {
                while (true) {
                    resources.getTimeAsLabeledOrNull(R.string.auto_refresh_time)?.let { delayMsec ->
                        delay(delayMsec)
                        loadData()
                    } ?: break
                }
            } catch (ce: CancellationException ){
                // expected, just ignore
            } catch (e: Exception) {
                // kill timer on any exception
                Timber.e(e, "Unexpected exception in refresh timer loop")
            }
        }
    }

    private fun kilAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }
}