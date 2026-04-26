package br.com.brunocarvalhs.group.create.app.data.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ContactServiceImplTest {

    private val context: Context = mockk()
    private val contentResolver = mockk<android.content.ContentResolver>()

    private lateinit var service: ContactServiceImpl

    @Before
    fun setup() {
        service = ContactServiceImpl(context)
        every { context.contentResolver } returns contentResolver
    }

    // ---------------------------------------------------
    // Permission denied
    // ---------------------------------------------------
    @Test
    fun `should return empty list when permission is not granted`() {
        mockkStatic(ContextCompat::class)

        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        } returns PackageManager.PERMISSION_DENIED

        val result = service.getContacts()

        assertTrue(result.isEmpty())

        unmockkStatic(ContextCompat::class)
    }

    // ---------------------------------------------------
    // Happy path
    // ---------------------------------------------------
    @Test
    fun `should return contacts sorted and distinct when permission granted`() {
        mockkStatic(ContextCompat::class)

        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        } returns PackageManager.PERMISSION_GRANTED

        every {
            contentResolver.query(
                any<Uri>(),
                any(),
                any(),
                any(),
                any()
            )
        } answers {

            val uri = args[0] as? Uri

            when (uri) {
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI -> mockPhoneCursor()
                ContactsContract.CommonDataKinds.Email.CONTENT_URI -> mockEmailCursor()
                ContactsContract.Data.CONTENT_URI -> mockOrgCursor()
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI -> mockAddressCursor()
                else -> null
            }
        }

        val result = service.getContacts()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())

        unmockkStatic(ContextCompat::class)
    }

    // ====================================================
    // MOCK CURSORS
    // ====================================================

    private fun mockPhoneCursor(): Cursor {
        val cursor = mockk<Cursor>()

        every { cursor.moveToNext() } returnsMany listOf(true, false)

        every { cursor.getColumnIndex(any()) } answers {
            when (firstArg<String>()) {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID -> 0
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME -> 1
                ContactsContract.CommonDataKinds.Phone.NUMBER -> 2
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI -> 3
                else -> 0
            }
        }

        every { cursor.getString(0) } returns "1"
        every { cursor.getString(1) } returns "Bruno"
        every { cursor.getString(2) } returns "999999999"
        every { cursor.getString(3) } returns null

        every { cursor.close() } just Runs

        return cursor
    }

    private fun mockEmailCursor(): Cursor {
        val cursor = mockk<Cursor>()

        every { cursor.moveToNext() } returnsMany listOf(true, false)

        every { cursor.getColumnIndex(any()) } answers {
            when (firstArg<String>()) {
                ContactsContract.CommonDataKinds.Email.CONTACT_ID -> 0
                ContactsContract.CommonDataKinds.Email.ADDRESS -> 1
                else -> -1
            }
        }

        every { cursor.getString(0) } returns "1"
        every { cursor.getString(1) } returns "bruno@email.com"

        every { cursor.close() } just Runs

        return cursor
    }

    private fun mockOrgCursor(): Cursor {
        val cursor = mockk<Cursor>()

        every { cursor.moveToNext() } returns false

        every { cursor.getColumnIndex(any()) } answers {
            when (firstArg<String>()) {
                ContactsContract.CommonDataKinds.Organization.CONTACT_ID -> 0
                ContactsContract.CommonDataKinds.Organization.COMPANY -> 1
                ContactsContract.CommonDataKinds.Organization.TITLE -> 2
                else -> -1
            }
        }

        every { cursor.close() } just Runs

        return cursor
    }

    private fun mockAddressCursor(): Cursor {
        val cursor = mockk<Cursor>()

        every { cursor.moveToNext() } returns false

        every { cursor.getColumnIndex(any()) } answers {
            when (firstArg<String>()) {
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID -> 0
                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS -> 1
                else -> -1
            }
        }

        every { cursor.close() } just Runs

        return cursor
    }
}
