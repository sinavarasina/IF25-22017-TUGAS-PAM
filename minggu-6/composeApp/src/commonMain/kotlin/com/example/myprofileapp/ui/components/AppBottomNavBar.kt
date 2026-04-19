package com.example.myprofileapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myprofileapp.navigation.BottomNavItem
import com.example.myprofileapp.ui.theme.Colors

@Composable
fun AppBottomBar(
    navController: NavController,
    colors: Colors,
) {
    val items = listOf(BottomNavItem.News, BottomNavItem.Notes, BottomNavItem.Favorites, BottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = colors.backgroundTopBar) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.backgroundMain,
                        selectedTextColor = colors.accentPrimary,
                        indicatorColor = colors.accentPrimary,
                        unselectedIconColor = colors.textSecondary,
                        unselectedTextColor = colors.textSecondary,
                    ),
            )
        }
    }
}
