package com.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.weatherforecast.feature.city.CityListFragment
import com.weatherforecast.feature.weather.WeatherHomeFragment
import com.weatherforecast.feature.weather.OnCitySelectionRequested

/**
 * 主 Activity
 */
class MainActivity : AppCompatActivity(), OnCitySelectionRequested {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if (savedInstanceState == null) {
            showWeatherHomeFragment()
        }
    }
    
    private fun showWeatherHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, WeatherHomeFragment())
            .commit()
    }
    
    fun showCityListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CityListFragment())
            .addToBackStack(null)
            .commit()
    }
    
    override fun showCitySelection() {
        showCityListFragment()
    }
    
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
