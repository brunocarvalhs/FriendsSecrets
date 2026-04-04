package br.com.brunocarvalhs.group.list.app.domain.services

import kotlin.reflect.KClass

interface StorageService {
    suspend fun <T> save(key: String, value: T)
    suspend fun <T : Any> load(key: String, value: KClass<T>): T?
}