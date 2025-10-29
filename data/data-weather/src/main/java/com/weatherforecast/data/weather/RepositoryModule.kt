package com.weatherforecast.data.weather

import com.weatherforecast.core.common.CityRepository
import com.weatherforecast.core.common.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
    
    single<CityRepository> { CityRepositoryImpl(get(), get()) }
}
