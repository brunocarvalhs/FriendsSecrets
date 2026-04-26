package br.com.brunocarvalhs.group.create.app.domain.services

import kotlinx.coroutines.flow.StateFlow

interface GroupImageService {
    val availablePhotos: StateFlow<List<String>>
    fun getDefault(): String
}
