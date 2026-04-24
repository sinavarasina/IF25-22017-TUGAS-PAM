# TUGAS 5 PAM - Navigation Component

> this app i made to fullfill my homework/task
<!--
> see [this module]() for further details.
-->
-----

## Student Identity

Name = Varasina Farmadani

NIM = 123140107

Class = PAM RA

## Media

### Video

[![demo](media/gif/gif5.0.gif)](media/video/record5.0.mp4)

> click the image to see the highres one

## Screenshoot
> its all in the gif/video, ig SS is not really needed, but just make issue if it needed, i will upload soon.

## Code Documentation

> its more likely Code Flow

### 1\. Centralized Navigation Routing

Instead of managing screens manually, I refactored the app to strictly follow the **Navigation Component** architecture. I created a specific Sealed Class (`Screen`) and managed them inside `Navigation.kt` using `NavHost` and `NavController`. The UI seamlessly navigates between composables based on the defined routes.

```kotlin
sealed class Screen(val route: String) {
    object Notes : Screen("notes_list")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
}
```

### 2\. Bottom Navigation Implementation

I refactored the main navigation flow into a robust `BottomNavigationBar` system. I mapped multiple navigation targets into a single generic `BottomNavItem` object. I also utilized `currentBackStackEntryAsState()` inside the `Scaffold` to handle active tab highlighting and switching seamlessly across the main screens.

```kotlin
// AppBottomBar.kt
val navBackStackEntry by navController.currentBackStackEntryAsState()
val currentRoute = navBackStackEntry?.destination?.route

NavigationBarItem(
    selected = currentRoute == item.route,
    onClick = {
        navController.navigate(item.route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
        }
    },
    // ...
)
```

### 3\. Passing Arguments Between Screens

To fulfill the specific data viewing requirement, I extracted the ID passing mechanism into the navigation routes. It doesn't hold hardcoded data; instead, it receives the `noteId` as an argument from the `NavHost` backstack and throws it to the destination screen (like `NoteDetailScreen` and `EditNoteScreen`).

```kotlin
composable(
    route = Screen.NoteDetail.route,
    arguments = listOf(navArgument("noteId") { type = NavType.IntType }),
) { backStackEntry ->
    val id = backStackEntry.arguments?.let { bundle ->
        NavType.IntType.get(bundle, "noteId")
    } ?: 0

    NoteDetailScreen(
        noteId = id,
        // ...
    )
}
```

### 4\. FAB Contextual Visibility & Back Stack Handling

I added a feature to conditionally show the Floating Action Button. When the user opens the `Notes` screen, the app uses the `currentRoute` to show the FAB. This ensures that the button is not visible on screens like Profile or Favorites, and proper back navigation (`popBackStack()`) is triggered when saving or closing forms.

```kotlin
val showFab = currentRoute == Screen.Notes.route

floatingActionButton = {
    AnimatedVisibility(visible = showFab) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddNote.route) },
            // ...
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
    }
}
```

-----

This is a Kotlin Multiplatform project targeting Android.

  \* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
    It contains several subfolders:
  \* `commonMain` is for code that’s common for all targets.
  \* Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the `iosMain` folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the `jvmMain`
    folder is the appropriate location.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

  \* on macOS/Linux

```shell
./gradlew :composeApp:assembleDebug

```

  \* on Windows

```shell
.\gradlew.bat :composeApp:assembleDebug

```

-----

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
