package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.ui.components.profile.InfoItem
import com.example.myprofileapp.ui.components.profile.ProfileCard
import com.example.myprofileapp.ui.components.profile.ProfileHeader
import com.example.myprofileapp.ui.style.colorscheme.CatppuccinMocha

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CatppuccinMocha.crust)
                        .statusBarsPadding()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CatppuccinMocha.text
                    )
                }
            },
            containerColor = CatppuccinMocha.base
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                color = CatppuccinMocha.base
            ) {
                var showDetailInfo by remember { mutableStateOf(false) }
                var isOnline by remember { mutableStateOf(true) }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ProfileCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileHeader(
                                name = "Varasina Farmadani",
                                bio = "a regular human, nothing special about me.",
                                statusColor = if (isOnline) CatppuccinMocha.green else CatppuccinMocha.red
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { showDetailInfo = !showDetailInfo },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CatppuccinMocha.mauve,
                                        contentColor = CatppuccinMocha.crust
                                    )
                                ) {
                                    Text(if (showDetailInfo) "Hide Detail" else "Show Detail")
                                }

                                Button(
                                    onClick = { isOnline = !isOnline },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isOnline) CatppuccinMocha.green else CatppuccinMocha.red,
                                        contentColor = CatppuccinMocha.crust
                                    )
                                ) {
                                    Text(if (isOnline) "Set Offline" else "Set Online")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = showDetailInfo) {
                        ProfileCard {
                            InfoItem(
                                icon = Icons.Default.Badge,
                                text = "123140107"
                            )
                            InfoItem(
                                icon = Icons.Default.Email,
                                text = "sina@sinanonym.my.id"
                            )
                            InfoItem(
                                icon = Icons.Default.Phone,
                                text = "+6285117788740"
                            )
                            InfoItem(
                                icon = Icons.Default.Link,
                                text = "sinanonym.my.id",
                                color = CatppuccinMocha.sapphire
                            )
                        }
                    }
                }
            }
        }
    }
}