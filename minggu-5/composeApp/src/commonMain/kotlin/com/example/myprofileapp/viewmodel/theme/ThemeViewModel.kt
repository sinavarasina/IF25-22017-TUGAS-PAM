package com.example.myprofileapp.viewmodel.theme

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeState
import com.example.myprofileapp.data.theme.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemeViewModel : ViewModel() {
    private val _themeState = MutableStateFlow(ThemeState())

    val themeState: StateFlow<ThemeState> = _themeState.asStateFlow()

    fun setThemeType(type: ThemeType) {
        _themeState.update { currentState ->
            currentState.copy(activeThemeType = type)
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeState.update { currentState ->
            currentState.copy(themeMode = mode)
        }
    }
}