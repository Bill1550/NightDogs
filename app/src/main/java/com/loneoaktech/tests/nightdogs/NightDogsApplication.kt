package com.loneoaktech.tests.nightdogs

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.loneoaktech.tests.nightdogs.di.AppComponent
import com.loneoaktech.tests.nightdogs.di.AppModule
import com.loneoaktech.tests.nightdogs.di.DaggerAppComponent
import timber.log.Timber

/**
 * Created by BillH on 3/1/2019
 */
class NightDogsApplication : Application() {

    companion object {
        private var APP_INSTANCE: NightDogsApplication? = null

        val graph: AppComponent
            get() = APP_INSTANCE!!.component
    }

    val component: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        APP_INSTANCE = this
        component.inject(this)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        AndroidThreeTen.init(this)
    }
}