package com.loneoaktech.tests.nightdogs.di

import com.loneoaktech.tests.nightdogs.MainActivity
import com.loneoaktech.tests.nightdogs.NightDogsApplication
import com.loneoaktech.tests.nightdogs.PetPixFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by BillH on 3/1/2019
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject( application: NightDogsApplication )
    fun inject( activity: MainActivity )
    fun inject( petPixFragment: PetPixFragment )

}