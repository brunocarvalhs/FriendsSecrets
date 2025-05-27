package br.com.brunocarvalhs.friendssecrets.domain.services

interface StorageService {
    fun <T> save(key: String, value: T)
    fun <T> load(key: String, clazz: Class<T>): T?
    fun remove(key: String)
}