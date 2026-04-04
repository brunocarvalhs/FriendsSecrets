package br.com.brunocarvalhs.group.create.app.domain.services

import android.net.Uri

interface ImageStorageService {
    suspend fun upload(uri: Uri, path: String): Result<String>
}