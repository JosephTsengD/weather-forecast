package com.weatherforecast.core.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar

/**
 * UI 工具類
 */
object UiUtils {
    
    /**
     * 載入天氣圖示
     */
    fun loadWeatherIcon(context: Context, imageView: ImageView, iconCode: String) {
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        
        Glide.with(context)
            .load(iconUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
    
    /**
     * 顯示錯誤訊息
     */
    fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
    
    /**
     * 顯示成功訊息
     */
    fun showSuccess(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
    
    /**
     * 格式化溫度
     */
    fun formatTemperature(temperature: Double): String {
        return "${temperature.toInt()}°C"
    }
    
    /**
     * 格式化風速
     */
    fun formatWindSpeed(windSpeed: Double): String {
        return "${windSpeed.toInt()} km/h"
    }
    
    /**
     * 格式化濕度
     */
    fun formatHumidity(humidity: Int): String {
        return "$humidity%"
    }
    
    /**
     * 格式化氣壓
     */
    fun formatPressure(pressure: Int): String {
        return "$pressure hPa"
    }
}
