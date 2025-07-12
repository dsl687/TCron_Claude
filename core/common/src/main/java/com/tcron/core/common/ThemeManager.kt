package com.tcron.core.common

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@HiltViewModel
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    private val _currentTheme = MutableStateFlow(getThemeMode())
    val currentTheme: StateFlow<ThemeMode> = _currentTheme.asStateFlow()
    
    fun setThemeMode(themeMode: ThemeMode) {
        _currentTheme.value = themeMode
        prefs.edit().putString("theme_mode", themeMode.name).apply()
    }
    
    private fun getThemeMode(): ThemeMode {
        val themeName = prefs.getString("theme_mode", ThemeMode.DARK.name)
        return ThemeMode.valueOf(themeName ?: ThemeMode.DARK.name)
    }
}