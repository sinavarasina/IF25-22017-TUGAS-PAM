package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.ui.components.AppTopBar
import com.example.myprofileapp.ui.components.profile.EditProfileCard
import com.example.myprofileapp.ui.components.profile.InfoItem
import com.example.myprofileapp.ui.components.profile.ProfileCard
import com.example.myprofileapp.ui.components.profile.ProfileHeader
import com.example.myprofileapp.ui.theme.Themes
import com.example.myprofileapp.viewmodel.profile.ProfileViewModel
import com.example.myprofileapp.viewmodel.theme.ThemeViewModel

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val themeState by themeViewModel.themeState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()

    val currentTheme = when (themeState.activeThemeType) {
        ThemeType.CATPPUCCIN -> Themes.Catppuccin
        ThemeType.GRUVBOX -> Themes.GruvBox
    }
    val colors =
        if (themeState.themeMode == ThemeMode.DARK) currentTheme.dark else currentTheme.light

    var draftName by remember(profileState.name) { mutableStateOf(profileState.name) }
    var draftBio by remember(profileState.bio) { mutableStateOf(profileState.bio) }
    var draftStudentId by remember(profileState.studentId) { mutableStateOf(profileState.studentId) }
    var draftEmail by remember(profileState.email) { mutableStateOf(profileState.email) }
    var draftPhone by remember(profileState.phone) { mutableStateOf(profileState.phone) }
    var draftWebsite by remember(profileState.website) { mutableStateOf(profileState.website) }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Profile",
                    colors = colors,
                    activeThemeType = themeState.activeThemeType,
                    themeMode = themeState.themeMode,
                    onThemeTypeChange = { newType -> themeViewModel.setThemeType(newType) },
                    onThemeModeChange = { newMode -> themeViewModel.setThemeMode(newMode) },
                    showThemeControls = true
                )
            },
            containerColor = colors.backgroundMain
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                color = colors.backgroundMain
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()) // <--- Ini kunci utamanya
                ) {
                    ProfileCard(colors = colors) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileHeader(
                                name = profileState.name,
                                bio = profileState.bio,
                                statusColor = if (profileState.isOnline) colors.success else colors.error,
                                colors = colors
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { profileViewModel.toggleDetailInfo() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.accentPrimary,
                                        contentColor = colors.backgroundMain
                                    )
                                ) {
                                    Text(if (profileState.showDetailInfo) "Hide Detail" else "Show Detail")
                                }

                                Button(
                                    onClick = { profileViewModel.toggleOnlineStatus() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (profileState.isOnline) colors.success else colors.error,
                                        contentColor = colors.backgroundMain
                                    )
                                ) {
                                    Text(if (profileState.isOnline) "Set Offline" else "Set Online")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { profileViewModel.toggleEditMode() }) {
                                Text(
                                    if (profileState.isEditing) "Cancel Edit" else "Edit Profile",
                                    color = colors.accentSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = profileState.isEditing) {
                        EditProfileCard(
                            draftName = draftName,
                            draftBio = draftBio,
                            draftStudentId = draftStudentId,
                            draftEmail = draftEmail,
                            draftPhone = draftPhone,
                            draftWebsite = draftWebsite,
                            onNameChange = { draftName = it },
                            onBioChange = { draftBio = it },
                            onStudentIdChange = { draftStudentId = it },
                            onEmailChange = { draftEmail = it },
                            onPhoneChange = { draftPhone = it },
                            onWebsiteChange = { draftWebsite = it },
                            onSaveClick = {
                                profileViewModel.updateProfile(
                                    newName = draftName,
                                    newBio = draftBio,
                                    newStudentId = draftStudentId,
                                    newEmail = draftEmail,
                                    newPhone = draftPhone,
                                    newWebsite = draftWebsite
                                )
                            },
                            colors = colors
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = profileState.showDetailInfo && !profileState.isEditing) {
                        ProfileCard(colors = colors) {
                            InfoItem(
                                icon = Icons.Default.Badge,
                                text = profileState.studentId,
                                textColor = colors.textPrimary,
                                colors = colors
                            )
                            InfoItem(
                                icon = Icons.Default.Email,
                                text = profileState.email,
                                textColor = colors.textPrimary,
                                colors = colors
                            )
                            InfoItem(
                                icon = Icons.Default.Phone,
                                text = profileState.phone,
                                textColor = colors.textPrimary,
                                colors = colors
                            )
                            InfoItem(
                                icon = Icons.Default.Link,
                                text = profileState.website,
                                textColor = colors.link,
                                colors = colors
                            )
                        }
                    }
                }
            }
        }
    }
}