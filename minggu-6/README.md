# TUGAS 6 PAM - Networking & REST API

> this app i made to fullfill my homework/task
-----

## Student Identity

Name = Varasina Farmadani

NIM = 123140107

Class = PAM RA

## Media

### Video
[![Demo](media/gif/gif6.0.gif)](media/video/record6.0.mp4)
> click the gif image to see the highres video.

## Screenshoot
> maybe later

## Code Documentation

> its more likely Code Flow

### 1\. Ktor Client Integration

Instead of using dummy local data, I refactored the app to fetch data dynamically from a public REST API. I implemented `Ktor Client` as the HTTP engine to handle network requests natively across Kotlin Multiplatform targets.

```kotlin
object HttpClientFactory {
    fun create(): HttpClient = HttpClient {
        install(ContentNegotiation.Plugin) {
            json(Json { 
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true 
            })
        }
        install(Logging.Companion) {
            level = LogLevel.BODY
        }
    }
}
```

### 2\. Kotlinx Serialization

To securely and type-safely map the raw JSON response into Kotlin objects, I integrated `kotlinx.serialization`. The `@Serializable` annotation seamlessly handles the parsing.

```kotlin
@Serializable
data class Article(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
```

### 3\. Repository Pattern

To fulfill the architecture separation requirement, I created a `NewsRepository` that acts as the single source of truth. It wraps the API calls inside a `Result` block to cleanly handle success and failure cases without leaking network exceptions directly to the ViewModel.

```kotlin
class NewsRepository(
    private val api: NewsApi,
    private val settings: Settings,
) {
    suspend fun getArticles(): Result<List<Article>> =
        try {
            val freshArticles = api.getArticles()
            // ... caching logic ...
            Result.success(freshArticles)
        } catch (e: Exception) {
            // ... fallback logic ...
        }
}
```

### 4\. Reactive UI States (Loading, Success, Error)

I extracted the UI state into a reusable `UiState` sealed class. The `NewsViewModel` now emits distinct states (`Loading`, `Success`, `Error`) using `StateFlow`. The UI uses a `when` expression to render the appropriate screen, including a `CircularProgressIndicator` for loading and a retry mechanism for errors.

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// In NewsListScreen.kt
when (val state = articlesState) {
    is UiState.Loading -> CircularProgressIndicator(color = colors.accentPrimary)
    is UiState.Success -> LazyColumn { /* render list */ }
    is UiState.Error -> Button(onClick = { viewModel.loadArticles() }) { Text("Try again") }
}
```

### 5\. Bonus (+10%): Offline Caching with Local Storage

To achieve a robust **Offline-First Architecture**, I implemented local caching using the `Russhwolf Multiplatform Settings` library. 

In the `NewsRepository`, whenever a network request via Ktor succeeds, the `List<Article>` is serialized into a JSON String and stored locally. If the user loses internet connection (e.g., Airplane mode), the repository catches the network exception, retrieves the JSON string from the local storage, deserializes it, and seamlessly feeds it back to the UI. The user can still read previously fetched news and view details completely offline!

```kotlin
} catch (e: Exception) {
    val cachedJson = settings.getStringOrNull(CACHE_KEY)
    if (cachedJson != null) {
        val cachedArticles = Json.Default.decodeFromString<List<Article>>(cachedJson)
        Result.success(cachedArticles)
    } else {
        Result.failure(Exception("No internet and no cached data."))
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
```
