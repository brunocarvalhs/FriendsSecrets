package br.com.brunocarvalhs.friendssecrets.domain.services

interface StorageService {
    suspend fun <T> save(key: String, value: T)
    suspend fun <T> load(key: String, clazz: Class<T>): T?
    suspend fun remove(key: String)
}