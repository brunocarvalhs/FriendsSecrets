package br.com.brunocarvalhs.friendssecrets.domain.services

import kotlin.reflect.KClass

interface StorageService {
    suspend fun <T: Any> save(key: String, value: T)
    suspend fun <T: Any> load(key: String, value: KClass<T>): T?
    suspend fun remove(key: String)
}