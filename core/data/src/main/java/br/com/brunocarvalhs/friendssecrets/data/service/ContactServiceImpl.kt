package br.com.brunocarvalhs.friendssecrets.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.ContactService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class ContactServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContactService {

    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getPhoneNumbers(): List<String> {
        if (!hasPermission(context)) return emptyList()

        val contentResolver = context.contentResolver
        val numbers = mutableListOf<String>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                if (number.isNotBlank()) {
                    numbers.add(number)
                }
            }
        }

        return numbers.distinct()
    }

    override fun getContacts(): List<UserEntities> {
        if (!hasPermission(context)) return emptyList()

        val contactList = mutableListOf<UserEntities>()
        val contentResolver = context.contentResolver

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            ),
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                val photoUri = it.getString(photoIndex)?.let { uri -> Uri.parse(uri) }

                if (number.isNotBlank()) {
                    contactList.add(
                        UserModel(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            phoneNumber = number,
                            photoUrl = photoUri.toString(),
                            isPhoneNumberVerified = false,
                            likes = emptyList()
                        )
                    )
                }
            }
        }

        return contactList.distinctBy { it.name }.sortedBy { it.name }
    }
}
