package br.com.brunocarvalhs.friendssecrets.domain.services

interface DeviceService {
    suspend fun getDeviceId(): String
}