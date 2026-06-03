package com.example.myprofileapp.platform

expect class DeviceInfo() {
    fun getDeviceName(): String

    fun getOsVersion(): String

    fun getAppVersion(): String
}
