package com.example.myprofileapp.platform

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String = UIDevice.currentDevice.name

    actual fun getOsVersion(): String =
        "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"

    actual fun getAppVersion(): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0"
}
