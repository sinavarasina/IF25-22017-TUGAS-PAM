package com.example.tugaspraktikumminggu1pam

class Greeting {
    private val platform = getPlatform()
    private val name = "Varasina Farmadani"
    private val nim = 123140107

    fun greet(): String {
        return "Hello, $name!"
    }
    fun getNIM(): String {
            return "NIM : ${nim.toString()}"
    }
    fun getCurPlatform(): String {
        return "Current platform : ${platform.name}"
    }
}