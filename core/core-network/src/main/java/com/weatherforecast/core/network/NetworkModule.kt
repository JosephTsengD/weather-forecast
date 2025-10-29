package com.weatherforecast.core.network

import org.koin.dsl.module

val networkModule = module {
    
    single { createOkHttpClient() }
    
    single { createRetrofit(get()) }
    
    single { get<retrofit2.Retrofit>().create(WeatherApi::class.java) }
}
