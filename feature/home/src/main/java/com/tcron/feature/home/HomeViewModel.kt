package com.tcron.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcron.core.domain.model.Task
import com.tcron.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadTasks()
    }
    
    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tasks = tasks,
                        error = null
                    )
                }
        }
    }
    
    fun refreshTasks() {
        loadTasks()
    }
    
    fun toggleTaskEnabled(taskId: String, enabled: Boolean) {
        viewModelScope.launch {
            taskRepository.toggleTaskEnabled(taskId, enabled)
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)