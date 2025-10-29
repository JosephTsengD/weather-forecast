package com.weatherforecast.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import android.content.Context

/**
 * 城市實體
 */
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isSelected: Boolean = false
)

/**
 * 天氣緩存實體
 */
@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val cityId: String,
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val timestamp: Long
)

/**
 * 城市 DAO
 */
@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>
    
    @Query("SELECT * FROM cities WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedCity(): CityEntity?
    
    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%'")
    suspend fun searchCities(query: String): List<CityEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)
    
    @Update
    suspend fun updateCity(city: CityEntity)
    
    @Delete
    suspend fun deleteCity(city: CityEntity)
    
    @Query("UPDATE cities SET isSelected = 0")
    suspend fun clearSelectedCities()
    
    @Query("UPDATE cities SET isSelected = 1 WHERE id = :cityId")
    suspend fun setSelectedCity(cityId: String)
}

/**
 * 天氣 DAO
 */
@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE cityId = :cityId")
    suspend fun getWeatherCache(cityId: String): WeatherCacheEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherCache(weather: WeatherCacheEntity)
    
    @Query("DELETE FROM weather_cache WHERE timestamp < :expireTime")
    suspend fun deleteExpiredCache(expireTime: Long)
    
    @Query("DELETE FROM weather_cache WHERE cityId = :cityId")
    suspend fun deleteWeatherCache(cityId: String)
}

/**
 * 天氣數據庫
 */
@Database(
    entities = [CityEntity::class, WeatherCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
    
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        
        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
