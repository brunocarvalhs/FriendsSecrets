package br.com.brunocarvalhs.deviceid

interface DeviceService {
    suspend fun getDeviceId(): String
}
