package com.weatherforecast

import android.app.Application
import com.weatherforecast.core.utils.ResourcesHelper
import com.weatherforecast.initKoin

/**
 * 天氣預報應用程式
 */
class WeatherApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        ResourcesHelper.init(this)
        initKoin(this)
    }
}
