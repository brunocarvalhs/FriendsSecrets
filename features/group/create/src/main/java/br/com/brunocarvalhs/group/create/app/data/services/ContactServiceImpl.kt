package br.com.brunocarvalhs.group.create.app.data.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.services.ContactService
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class ContactServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ContactService {

    @AddTrace(name = "ContactServiceImpl.hasPermission", enabled = true)
    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @AddTrace(name = "ContactServiceImpl.getContacts", enabled = true)
    override fun getContacts(): List<ContactModel> {
        if (!hasPermission(context)) return emptyList()

        val contactList = mutableListOf<ContactModel>()
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
                        ContactModel(
                            name = name,
                            phoneNumber = number,
                            photoUrl = photoUri.toString(),
                        )
                    )
                }
            }
        }

        return contactList.distinctBy { it.name }.sortedBy { it.name }
    }
}
