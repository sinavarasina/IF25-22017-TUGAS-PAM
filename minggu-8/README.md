# TUGAS 8 PAM - Platform-Specific Features

> this app i made to fullfill my homework/task
-----

## Student Identity

Name = Varasina Farmadani

NIM = 123140107

Class = PAM RA

## Media

### Video
[![Demo](media/gif/gif8.0.gif)](media/video/record8.0.webm)
> click the gif image to see the highres video.

<!-- ## Screenshoot -->
<!---->
<!-- ### Device Info on Settings Screen -->
<!-- ![Device Info](media/screenshot/device-info.png) -->
<!---->
<!-- ### Network Status Indicator -->
<!-- ![Network Status](media/screenshot/network-status.png) -->

## Code Documentation

### 1. Koin Dependency Injection Setup

To make the app more modular and easier to maintain, I implemented `Koin` as the Dependency Injection framework. Instead of creating repositories, services, database, settings, and view models directly inside the UI layer, all dependencies are now registered in a single Koin module.

```kotlin
// di/AppModule.kt
val appModule = module {
    single { Settings() }
    single { SettingsManager(get()) }

    single { createDatabaseDriver() }
    single { NotesDatabase(get()) }

    single { HttpClientFactory.create() }
    single { NewsApi(get()) }

    single { NoteRepository(get()) }
    single { NewsRepository(get(), get()) }

    single { DeviceInfo() }
    single { NetworkMonitor() }

    single { ThemeViewModel() }
    single { ProfileViewModel() }
    single { NotesViewModel(get(), get()) }
    single { NewsViewModel(get()) }
}
```

Then the module is initialized from the root composable using `KoinApplication`. This keeps the object creation flow centralized and prevents the UI from becoming a dependency factory. Basically, less spaghetti, more clean DI.

```kotlin
@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        AppContent()
    }
}
```

### 2. Platform-Specific DeviceInfo with expect/actual

To access native device information from shared code, I created a `DeviceInfo` class using the `expect`/`actual` mechanism. The common code only defines the API, while each platform provides its own implementation.

```kotlin
// commonMain/platform/DeviceInfo.kt
expect class DeviceInfo() {
    fun getDeviceName(): String
    fun getOsVersion(): String
    fun getAppVersion(): String
}
```

On Android, the implementation uses Android platform APIs such as `Build.MANUFACTURER`, `Build.MODEL`, and `Build.VERSION`. The application version is retrieved from the package manager.

```kotlin
// androidMain/platform/DeviceInfo.android.kt
actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String =
        listOf(Build.MANUFACTURER, Build.MODEL)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .replaceFirstChar { it.uppercase() }

    actual fun getOsVersion(): String =
        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    actual fun getAppVersion(): String =
        runCatching {
            appContext.packageManager
                .getPackageInfo(appContext.packageName, 0)
                .versionName ?: "1.0"
        }.getOrDefault("1.0")
}
```

On iOS, the same common API is implemented with `UIDevice` and `NSBundle`. This allows the shared UI to call the same functions without knowing the platform detail behind it.

```kotlin
// iosMain/platform/DeviceInfo.ios.kt
actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String = UIDevice.currentDevice.name

    actual fun getOsVersion(): String =
        "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"

    actual fun getAppVersion(): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0"
}
```

### 3. NetworkMonitor with expect/actual

To monitor internet connectivity, I implemented a `NetworkMonitor` using `expect`/`actual`. The common declaration exposes a simple API: one function for checking the current state and one reactive `Flow` for observing network changes.

```kotlin
// commonMain/platform/NetworkMonitor.kt
expect class NetworkMonitor() {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}
```

On Android, `NetworkMonitor` uses `ConnectivityManager`, `NetworkCapabilities`, and `callbackFlow`. This makes the connection status reactive, so the UI can update automatically when the device goes online or offline.

```kotlin
// androidMain/platform/NetworkMonitor.android.kt
actual class NetworkMonitor actual constructor() {
    private val connectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    actual fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    actual fun observeConnectivity(): Flow<Boolean> =
        callbackFlow {
            trySend(isConnected())

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(isConnected())
                }

                override fun onLost(network: Network) {
                    trySend(isConnected())
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(request, callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
}
```

For iOS, the current implementation uses a simple stub that returns `true`. This keeps the multiplatform build valid while the Android implementation is used as the main tested platform for this task.

```kotlin
// iosMain/platform/NetworkMonitor.ios.kt
actual class NetworkMonitor actual constructor() {
    actual fun isConnected(): Boolean = true

    actual fun observeConnectivity(): Flow<Boolean> = MutableStateFlow(true)
}
```

### 4. Device Info on Settings Screen

The Settings screen now displays platform information by injecting `DeviceInfo` with Koin. This means the UI does not create the platform service directly; it only requests the dependency from the DI container.

```kotlin
@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    notesViewModel: NotesViewModel,
    colors: Colors,
    onThemeTypeChange: (ThemeType) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    val deviceInfo: DeviceInfo = koinInject()

    ProfileCard(colors = colors) {
        Text("Device Info", fontWeight = FontWeight.Bold, color = colors.textPrimary)
        SettingsInfoRow(label = "Device", value = deviceInfo.getDeviceName(), colors = colors)
        SettingsInfoRow(label = "OS", value = deviceInfo.getOsVersion(), colors = colors)
        SettingsInfoRow(label = "App Version", value = deviceInfo.getAppVersion(), colors = colors)
    }
}
```

This fulfills the platform API integration requirement because the shared Compose UI can display native device data without hardcoding Android-only code inside `commonMain`.

### 5. Network Status Indicator in Main Screen

The main screen now observes `NetworkMonitor.observeConnectivity()` and passes the state to the top bar. The top bar displays an Online/Offline pill, making the network state visible to the user.

```kotlin
val networkMonitor: NetworkMonitor = koinInject()
val isNetworkConnected by networkMonitor
    .observeConnectivity()
    .collectAsState(initial = networkMonitor.isConnected())

AppTopBar(
    title = appState.title,
    colors = colors,
    activeThemeType = themeState.activeThemeType,
    themeMode = themeState.themeMode,
    onThemeTypeChange = { type -> settingsManager.themeType = type },
    onThemeModeChange = { mode -> settingsManager.themeMode = mode },
    isNetworkConnected = isNetworkConnected,
)
```

Inside `AppTopBar`, the indicator is rendered as a compact status pill. Green means online, red means offline. Simple, readable, and not too dramatic like an anime villain monologue.

```kotlin
@Composable
private fun NetworkStatusPill(
    isConnected: Boolean,
    colors: Colors,
) {
    val statusText = if (isConnected) "Online" else "Offline"
    val statusColor = if (isConnected) colors.success else colors.error

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(statusColor.copy(alpha = 0.14f), RoundedCornerShape(100))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(statusColor, RoundedCornerShape(100)),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = statusText, color = statusColor, fontWeight = FontWeight.Bold)
    }
}
```
-----

This is a Kotlin Multiplatform project targeting Android.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
* `commonMain` is for code that’s common for all targets.
* Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
  For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
  the `iosMain` folder would be the right place for such calls.
  Similarly, if you want to edit the Desktop (JVM) specific part, the `jvmMain`
  folder is the appropriate location.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

* on macOS/Linux

```shell
./gradlew :composeApp:assembleDebug
```

* on Windows

```shell
.\gradlew.bat :composeApp:assembleDebug
```

-----

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
