package br.com.brunocarvalhs.group.create.app.data.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import br.com.brunocarvalhs.group.create.app.data.model.ContactDTO
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.services.ContactService
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ContactService {

    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @AddTrace(name = "ContactServiceImpl.getContacts", enabled = true)
    override fun getContacts(): List<ContactModel> {
        if (!hasPermission(context)) return emptyList()

        val contactMap = mutableMapOf<String, ContactDTO>()
        val contentResolver = context.contentResolver

        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            ),
            null,
            null,
            null
        )

        phoneCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex).orEmpty()
                val number = cursor.getString(numberIndex).orEmpty()
                val photoUri = cursor.getString(photoIndex)

                if (number.isNotBlank()) {
                    contactMap[id] = ContactDTO(
                        id = id,
                        displayName = name,
                        phoneNumber = number,
                        photoUri = photoUri
                    )
                }
            }
        }

        val emailCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.CONTACT_ID, ContactsContract.CommonDataKinds.Email.ADDRESS),
            null,
            null,
            null
        )
        emailCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                contactMap[id]?.let { contactMap[id] = it.copy(email = cursor.getString(emailIndex)) }
            }
        }

        val orgCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Organization.CONTACT_ID, ContactsContract.CommonDataKinds.Organization.COMPANY, ContactsContract.CommonDataKinds.Organization.TITLE),
            ContactsContract.Data.MIMETYPE + CURSOR_SELECTION,
            arrayOf(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE),
            null
        )
        orgCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.CONTACT_ID)
            val companyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
            val titleIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                contactMap[id]?.let {
                    contactMap[id] = it.copy(
                        company = cursor.getString(companyIndex),
                        jobTitle = cursor.getString(titleIndex)
                    )
                }
            }
        }

        val addressCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID, ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS),
            null,
            null,
            null
        )
        addressCursor?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID)
            val addrIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                contactMap[id]?.let { contactMap[id] = it.copy(address = cursor.getString(addrIndex)) }
            }
        }

        return contactMap.values
            .map { it.toDomain() }
            .distinctBy { it.phoneNumber }
            .sortedBy { it.name }
    }

    companion object {
        private const val CURSOR_SELECTION = " = ?"
    }
}
