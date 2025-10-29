package com.weatherforecast.core.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * 資源輔助類，用於在沒有 Context 的地方獲取字串資源
 */
object ResourcesHelper {
    
    private var applicationContext: Context? = null
    
    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun getString(@StringRes resId: Int): String {
        return applicationContext?.getString(resId)
            ?: throw IllegalStateException("ResourcesHelper not initialized. Call init() in Application.onCreate()")
    }
}

