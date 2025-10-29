package com.weatherforecast

import com.weatherforecast.core.database.databaseModule
import com.weatherforecast.core.network.networkModule
import com.weatherforecast.data.weather.repositoryModule
import com.weatherforecast.feature.city.cityViewModelModule
import com.weatherforecast.feature.weather.weatherViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Koin 模組配置
 */
val appModule = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    weatherViewModelModule,
    cityViewModelModule
)

/**
 * 初始化 Koin
 */
fun initKoin(context: android.content.Context) {
    startKoin {
        androidContext(context)
        modules(appModule)
    }
}
