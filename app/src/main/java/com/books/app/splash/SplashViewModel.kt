package com.books.app.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.common.Consumable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private var navigate: Unit? = null
        set(value) {
            field = value
            updateState()
        }

    private val state
        get() = SplashUiModel(
            navigate = navigate?.let { Consumable(it) { navigate = null } },
        )

    private val _splashUiState = MutableStateFlow(state)
    val splashUiState = _splashUiState.asStateFlow()

    private fun updateState() {
        _splashUiState.update { state }
    }

    init {
        viewModelScope.launch {
            delay(2000L)
            navigate = Unit
        }
    }
}

data class SplashUiModel(
    val navigate: Consumable<Unit>?,
)
