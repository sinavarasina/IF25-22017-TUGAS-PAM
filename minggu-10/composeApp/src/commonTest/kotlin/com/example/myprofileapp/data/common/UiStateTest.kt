package com.example.myprofileapp.data.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UiStateTest {
    @Test
    fun `loading is loading state`() {
        val state: UiState<String> = UiState.Loading

        assertIs<UiState.Loading>(state)
    }

    @Test
    fun `success stores data`() {
        val state = UiState.Success(listOf("A", "B"))

        assertEquals(listOf("A", "B"), state.data)
    }

    @Test
    fun `success equality is based on data`() {
        assertEquals(UiState.Success("OK"), UiState.Success("OK"))
    }

    @Test
    fun `error stores message`() {
        val state = UiState.Error("Network error")

        assertEquals("Network error", state.message)
    }

    @Test
    fun `error equality is based on message`() {
        assertEquals(UiState.Error("E"), UiState.Error("E"))
    }
}
