package com.cinescape1.di

import com.cinescape1.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class, ViewModelModule::class])
interface AppComponent : AndroidInjector<MainApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MainApplication): Builder
        fun build(): AppComponent
    }
}