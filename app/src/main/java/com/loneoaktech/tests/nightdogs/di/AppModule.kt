package com.loneoaktech.tests.nightdogs.di

import com.loneoaktech.tests.nightdogs.NightDogsApplication
import com.loneoaktech.tests.nightdogs.data.repo.AstronomicalRepo
import com.loneoaktech.tests.nightdogs.data.repo.AstronomicalRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by BillH on 3/1/2019
 */
@Module
class AppModule(val application: NightDogsApplication) {

    @Provides @Singleton fun providesApplication() = application

    @Provides internal fun providesAstronomicalRepo( repo: AstronomicalRepoImpl ): AstronomicalRepo = repo

}