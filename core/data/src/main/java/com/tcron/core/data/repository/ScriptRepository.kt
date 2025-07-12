package com.tcron.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class Script(
    val id: String,
    val name: String,
    val description: String,
    val content: String,
    val type: ScriptType,
    val requiresRoot: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ScriptType {
    PYTHON, SHELL
}

@Singleton
class ScriptRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tcron_scripts", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _scripts = MutableStateFlow(getScripts())
    val scripts: StateFlow<List<Script>> = _scripts.asStateFlow()
    
    fun saveScript(script: Script) {
        val currentScripts = getScripts().toMutableList()
        val existingIndex = currentScripts.indexOfFirst { it.id == script.id }
        
        if (existingIndex >= 0) {
            currentScripts[existingIndex] = script
        } else {
            currentScripts.add(script)
        }
        
        val json = gson.toJson(currentScripts)
        prefs.edit().putString("scripts", json).apply()
        _scripts.value = currentScripts
    }
    
    fun deleteScript(scriptId: String) {
        val currentScripts = getScripts().toMutableList()
        currentScripts.removeAll { it.id == scriptId }
        
        val json = gson.toJson(currentScripts)
        prefs.edit().putString("scripts", json).apply()
        _scripts.value = currentScripts
    }
    
    fun getScriptById(id: String): Script? {
        return getScripts().find { it.id == id }
    }
    
    private fun getScripts(): List<Script> {
        val json = prefs.getString("scripts", null) ?: return emptyList()
        val type = object : TypeToken<List<Script>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}