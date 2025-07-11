package com.tcron.core.common

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    var currentTheme by mutableStateOf(getThemeMode())
        private set
    
    fun setThemeMode(themeMode: ThemeMode) {
        currentTheme = themeMode
        prefs.edit().putString("theme_mode", themeMode.name).apply()
    }
    
    private fun getThemeMode(): ThemeMode {
        val themeName = prefs.getString("theme_mode", ThemeMode.DARK.name)
        return ThemeMode.valueOf(themeName ?: ThemeMode.DARK.name)
    }
}