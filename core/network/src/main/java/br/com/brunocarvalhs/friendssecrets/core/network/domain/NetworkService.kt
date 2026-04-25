package br.com.brunocarvalhs.friendssecrets.core.network.domain

import kotlin.reflect.KClass

interface NetworkService {

    suspend fun <T : Any> make(
        endpoint: String,
        payload: Map<String, Any?>? = null,
        headers: Map<String, String>? = null,
        query: Map<String, Any>? = null,
        method: Method,
        clazz: KClass<T>
    ): T?

    enum class Method {
        GET,
        POST,
        PUT,
        DELETE,
    }
}
