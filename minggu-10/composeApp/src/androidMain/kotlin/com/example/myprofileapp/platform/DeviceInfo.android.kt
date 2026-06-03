package com.example.myprofileapp.platform

import android.os.Build
import com.example.myprofileapp.appContext

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
