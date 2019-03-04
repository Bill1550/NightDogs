package com.loneoaktech.tests.nightdogs.di

import android.app.Application
import com.loneoaktech.tests.nightdogs.BuildConfig
import com.loneoaktech.tests.nightdogs.api.adapters.ApiMoshiFactory
import com.loneoaktech.tests.nightdogs.api.pets.PetPixApiFactory
import com.loneoaktech.tests.nightdogs.api.pets.PetPixResponse
import com.loneoaktech.tests.nightdogs.api.sun.SunApiRetrofitFactory
import com.loneoaktech.tests.nightdogs.data.repo.*
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by BillH on 3/1/2019
 */
@Module
class AppModule(val application: Application) {

    @Provides @Singleton fun providesApplication() = application

    @Provides internal fun providesAstronomicalRepo( repo: AstronomicalRepoImpl ): AstronomicalRepo = repo

    @Provides internal fun providesLocationRepo( repo: LocationRepoImpl ): LocationRepo = repo

    @Provides internal fun providePetPixRepo( repo: PetPixRepoImpl ): PetPixRepo = repo

    @Provides @Singleton fun provideMoshi() = ApiMoshiFactory.create()


    @Provides @Singleton internal fun providesSunApi(moshi: Moshi) = SunApiRetrofitFactory.create(moshi)

    @Provides @Singleton internal fun providePetPixApi(moshi: Moshi) = PetPixApiFactory.create(moshi)

    @Provides
    @Singleton
    internal fun providePicassoInstance(application: Application): Picasso {
        return Picasso.Builder( application )
            .indicatorsEnabled( BuildConfig.DEBUG )
            .loggingEnabled( false ) //  BuildConfig.DEBUG )
            .build()
    }
}