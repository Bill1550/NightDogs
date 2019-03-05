package com.loneoaktech.experimental.nightdogs.di

import com.loneoaktech.experimental.nightdogs.MainActivity
import com.loneoaktech.experimental.nightdogs.NightDogsApplication
import com.loneoaktech.experimental.nightdogs.PetPixFragment
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