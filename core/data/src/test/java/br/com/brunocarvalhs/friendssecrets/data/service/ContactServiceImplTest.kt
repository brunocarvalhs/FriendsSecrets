package br.com.brunocarvalhs.friendssecrets.data.service

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ContactServiceImplTest {
    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver
    private lateinit var cursor: Cursor
    private lateinit var service: ContactServiceImpl

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        contentResolver = mockk(relaxed = true)
        cursor = mockk(relaxed = true)
        every { context.contentResolver } returns contentResolver
        mockkStatic(ContextCompat::class)
        service = ContactServiceImpl(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getPhoneNumbers returns empty list when no permission`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        val result = service.getPhoneNumbers()
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `getPhoneNumbers returns phone numbers when permission granted`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_GRANTED
        every { contentResolver.query(any<Uri>(), any(), any(), any(), any()) } returns cursor
        every { cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) } returns 0
        every { cursor.moveToNext() } returnsMany listOf(true, true, false)
        every { cursor.getString(0) } returnsMany listOf("12345", "67890")
        val result = service.getPhoneNumbers()
        assertEquals(listOf("12345", "67890"), result)
    }

    @Test
    fun `getContacts returns empty list when no permission`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        val result = service.getContacts()
        assertEquals(emptyList<UserEntities>(), result)
    }

    @Test
    fun `getContacts returns contacts when permission granted`() {
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_GRANTED
        every { contentResolver.query(any<Uri>(), any(), any(), any(), any()) } returns cursor
        every { cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) } returns 0
        every { cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) } returns 1
        every { cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI) } returns 2
        every { cursor.moveToNext() } returnsMany listOf(true, false)
        every { cursor.getString(0) } returns "John Doe"
        every { cursor.getString(1) } returns "12345"
        every { cursor.getString(2) } returns "content://photo/1"
        val result = service.getContacts()
        assertEquals(1, result.size)
        val user = result[0] as UserModel
        assertEquals("John Doe", user.name)
        assertEquals("12345", user.phoneNumber)
        assertEquals("content://photo/1", user.photoUrl)
    }
}

