package com.weatherforecast.core.database

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    
    single { WeatherDatabase.getDatabase(androidContext()) }
    
    single { get<WeatherDatabase>().cityDao() }
    
    single { get<WeatherDatabase>().weatherDao() }
}
