package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myprofileapp.ui.components.profile.InfoItem
import com.example.myprofileapp.ui.components.profile.ProfileCard
import com.example.myprofileapp.ui.components.profile.ProfileHeader
import com.example.myprofileapp.ui.style.colorscheme.CatppuccinMocha

import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = CatppuccinMocha.base
        ) {
            var showDetailInfo by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ProfileHeader(
                    name = "Varasina Farmadani",
                    bio = "a regular human, nothing special about me."
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    showDetailInfo = !showDetailInfo
                }) {
                    Text(if (showDetailInfo) "hide detail" else "show detail")
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
                            icon = Icons.Default.Web,
                            text = "sinanonym.my.id"
                        )
                    }
                }
            }
        }
    }
}