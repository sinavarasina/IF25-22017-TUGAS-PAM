package com.example.myprofileapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.settings.SettingsManager
import com.example.myprofileapp.data.theme.ThemeMode
import com.example.myprofileapp.data.theme.ThemeType
import com.example.myprofileapp.platform.DeviceInfo
import com.example.myprofileapp.ui.components.profile.ProfileCard
import com.example.myprofileapp.ui.theme.Colors
import com.example.myprofileapp.viewmodel.notes.NotesViewModel
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    notesViewModel: NotesViewModel,
    colors: Colors,
    onThemeTypeChange: (ThemeType) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    val deviceInfo: DeviceInfo = koinInject()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileCard(colors = colors) {
            Text("Device Info", fontWeight = FontWeight.Bold, color = colors.textPrimary, fontSize = 16.sp)
            SettingsInfoRow(label = "Device", value = deviceInfo.getDeviceName(), colors = colors)
            SettingsInfoRow(label = "OS", value = deviceInfo.getOsVersion(), colors = colors)
            SettingsInfoRow(label = "App Version", value = deviceInfo.getAppVersion(), colors = colors)
        }

        ProfileCard(colors = colors) {
            Text("Theme", fontWeight = FontWeight.Bold, color = colors.textPrimary, fontSize = 16.sp)

            ThemeType.entries.forEach { type ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = settingsManager.themeType == type,
                        onClick = {
                            settingsManager.themeType = type
                            onThemeTypeChange(type)
                        },
                    )
                    Text(type.name, color = colors.textPrimary)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Dark Mode", color = colors.textPrimary)
                Switch(
                    checked = settingsManager.themeMode == ThemeMode.DARK,
                    onCheckedChange = { checked ->
                        val mode = if (checked) ThemeMode.DARK else ThemeMode.LIGHT
                        settingsManager.themeMode = mode
                        onThemeModeChange(mode)
                    },
                    colors =
                        SwitchDefaults.colors(
                            checkedTrackColor = colors.accentPrimary,
                            checkedThumbColor = colors.backgroundMain,
                        ),
                )
            }
        }
    }
}

@Composable
private fun SettingsInfoRow(
    label: String,
    value: String,
    colors: Colors,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = colors.textSecondary, fontSize = 14.sp)
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
