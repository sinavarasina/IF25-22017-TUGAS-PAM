package com.example.myprofileapp.viewmodel.profile

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.data.profile.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateProfile(
        newName: String,
        newBio: String,
        newStudentId: String,
        newPhone: String,
        newEmail: String,
        newWebsite: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                name = newName,
                bio = newBio,
                studentId = newStudentId,
                email = newEmail,
                phone = newPhone,
                website = newWebsite,
                isEditing = false,
            )
        }
    }

    fun toggleOnlineStatus() {
        _uiState.update { currentState ->
            currentState.copy(isOnline = !currentState.isOnline)
        }
    }

    fun toggleDetailInfo() {
        _uiState.update { currentState ->
            currentState.copy(showDetailInfo = !currentState.showDetailInfo)
        }
    }

    fun toggleEditMode() {
        _uiState.update { currentState ->
            currentState.copy(isEditing = !currentState.isEditing)
        }
    }
}
