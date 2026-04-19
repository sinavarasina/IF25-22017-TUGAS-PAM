package com.example.myprofileapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = colors.backgroundTopBar) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
