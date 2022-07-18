package com.cinescape1.di

import android.content.Context
import com.cinescape1.MainApplication
import com.cinescape1.data.network.DataServices
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.utils.Constant
import dagger.Module
import dagger.Provides

@Module
class AppModule{
    @Provides
    fun provideApi(consultantPreferences: AppPreferences): DataServices{
        return  DataServices.invoke(consultantPreferences.getString(""), consultantPreferences.getString(
            Constant.IntentKey.SELECT_LANGUAGE).toString()
        )
    }

    @Provides
    fun contextApp(application: MainApplication): Context {
        return application
    }

}