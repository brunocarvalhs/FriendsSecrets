package br.com.brunocarvalhs.friendssecrets.data.service

import android.content.Context
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

class ContactService() {

    private fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPhoneNumbers(context: Context): List<String> {
        if (!hasPermission(context)) return emptyList()

        val contentResolver = context.contentResolver
        val contactList = mutableListOf<String>()

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
                var number = it.getString(numberIndex)
                number = normalizePhoneNumber(number)
                if (number.isNotBlank()) {
                    contactList.add(number)
                }
            }
        }

        return contactList.distinct()
    }

    private fun normalizePhoneNumber(phone: String): String {
        return phone
            .replace("[^\\d+]".toRegex(), "") // Remove tudo que não for número ou +
            .replace("^\\+?55".toRegex(), "") // Remove DDI do Brasil, se tiver
            .trim()
    }
}
