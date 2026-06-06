package com.example.myprofileapp.viewmodel.profile

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProfileViewModelTest {
    @Test
    fun `default profile state is populated`() {
        val viewModel = ProfileViewModel()
        val state = viewModel.uiState.value

        assertEquals("Varasina Farmadani", state.name)
        assertEquals("123140107", state.studentId)
        assertEquals("sina@sinanonym.my.id", state.email)
        assertTrue(state.isOnline)
        assertFalse(state.showDetailInfo)
        assertFalse(state.isEditing)
    }

    @Test
    fun `updateProfile changes every editable field`() {
        val viewModel = ProfileViewModel()

        viewModel.updateProfile(
            newName = "New Name",
            newBio = "New Bio",
            newStudentId = "999",
            newPhone = "0800",
            newEmail = "new@example.com",
            newWebsite = "example.com",
        )

        val state = viewModel.uiState.value
        assertEquals("New Name", state.name)
        assertEquals("New Bio", state.bio)
        assertEquals("999", state.studentId)
        assertEquals("0800", state.phone)
        assertEquals("new@example.com", state.email)
        assertEquals("example.com", state.website)
    }

    @Test
    fun `updateProfile exits edit mode`() {
        val viewModel = ProfileViewModel()

        viewModel.toggleEditMode()
        assertTrue(viewModel.uiState.value.isEditing)

        viewModel.updateProfile(
            newName = "Name",
            newBio = "Bio",
            newStudentId = "1",
            newPhone = "2",
            newEmail = "a@b.c",
            newWebsite = "site",
        )

        assertFalse(viewModel.uiState.value.isEditing)
    }

    @Test
    fun `toggleOnlineStatus flips online flag`() {
        val viewModel = ProfileViewModel()

        viewModel.toggleOnlineStatus()
        assertFalse(viewModel.uiState.value.isOnline)

        viewModel.toggleOnlineStatus()
        assertTrue(viewModel.uiState.value.isOnline)
    }

    @Test
    fun `toggleDetailInfo flips detail flag`() {
        val viewModel = ProfileViewModel()

        viewModel.toggleDetailInfo()
        assertTrue(viewModel.uiState.value.showDetailInfo)

        viewModel.toggleDetailInfo()
        assertFalse(viewModel.uiState.value.showDetailInfo)
    }

    @Test
    fun `toggleEditMode flips edit flag`() {
        val viewModel = ProfileViewModel()

        viewModel.toggleEditMode()
        assertTrue(viewModel.uiState.value.isEditing)

        viewModel.toggleEditMode()
        assertFalse(viewModel.uiState.value.isEditing)
    }

    @Test
    fun `updateProfile keeps online and detail flags unchanged`() {
        val viewModel = ProfileViewModel()
        viewModel.toggleOnlineStatus()
        viewModel.toggleDetailInfo()

        viewModel.updateProfile(
            newName = "Name",
            newBio = "Bio",
            newStudentId = "1",
            newPhone = "2",
            newEmail = "a@b.c",
            newWebsite = "site",
        )

        assertFalse(viewModel.uiState.value.isOnline)
        assertTrue(viewModel.uiState.value.showDetailInfo)
    }
}
