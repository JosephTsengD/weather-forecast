package com.weatherforecast.core.common

/**
 * 天氣實體
 */
data class CurrentWeather(
    val cityId: String,
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 每日天氣預報
 */
data class DailyForecast(
    val date: String,
    val dayOfWeek: String,
    val highTemp: Double,
    val lowTemp: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double
)

/**
 * 城市實體
 */
data class City(
    val id: String,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isSelected: Boolean = false
)

/**
 * 結果封裝類
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }
    
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }
}

/**
 * 擴展函數
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}

fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

fun <T> Result<T>.isError(): Boolean = this is Result.Error
