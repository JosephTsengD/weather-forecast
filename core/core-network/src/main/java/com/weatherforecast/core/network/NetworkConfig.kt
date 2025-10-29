package com.weatherforecast.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * 網路配置類
 */
object NetworkConfig {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val API_KEY: String
        get() {
            val key = BuildConfig.WEATHER_API_KEY
            android.util.Log.d("NetworkConfig", "API Key 長度: ${key.length}, 前4位: ${key.take(4)}")
            return key
        }
}

/**
 * 創建 OkHttpClient
 */
fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

/**
 * 創建 Retrofit 實例
 */
fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
