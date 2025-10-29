package com.weatherforecast.feature.weather

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val weatherViewModelModule = module {
    
    viewModelOf(::WeatherViewModel)
}
