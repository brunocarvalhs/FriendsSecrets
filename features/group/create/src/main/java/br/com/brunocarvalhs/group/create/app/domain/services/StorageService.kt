package br.com.brunocarvalhs.group.create.app.domain.services

import kotlin.reflect.KClass

interface StorageService {
    suspend fun <T : Any> save(key: String, value: KClass<T>)
    suspend fun <T : Any> load(key: String, value: KClass<T>): T?
}