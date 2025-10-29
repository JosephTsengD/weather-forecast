package com.weatherforecast.feature.city

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val cityViewModelModule = module {
    
    viewModelOf(::CityListViewModel)
}
