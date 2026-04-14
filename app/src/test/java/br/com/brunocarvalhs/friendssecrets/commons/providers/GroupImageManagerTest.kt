package br.com.brunocarvalhs.friendssecrets.commons.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GroupImageManagerTest {

    private val remoteConfig: FirebaseRemoteConfig = mockk(relaxed = true)
    private lateinit var groupImageManager: GroupImageManager

    @Before
    fun setup() {
        // Mocking the task for fetchAndActivate
        val mockTask = mockk<Task<Boolean>>(relaxed = true)
        every { remoteConfig.fetchAndActivate() } returns mockTask
        
        groupImageManager = GroupImageManager(remoteConfig)
    }

    @Test
    fun `getDefault should return the first photo from available list`() {
        // When
        val result = groupImageManager.getDefault()

        // Then
        // Should return the first from defaultPhotos list initially
        assertEquals("https://images.unsplash.com/photo-1549465220-1a8b9238cd48?w=400&h=400&fit=crop", result)
    }

    @Test
    fun `setupRemoteConfig should configure settings and defaults`() {
        // Then
        verify { remoteConfig.setConfigSettingsAsync(any()) }
        verify { remoteConfig.setDefaultsAsync(any<Map<String, String>>()) }
        verify { remoteConfig.fetchAndActivate() }
    }
}
