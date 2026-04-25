package br.com.brunocarvalhs.friendssecrets.core.network.domain

import kotlin.reflect.KClass

interface NetworkService {

    suspend fun <T : Any> make(
        request: NetworkRequest,
        response: KClass<T>
    ): T?

    enum class Method {
        GET,
        POST,
        PUT,
        DELETE,
    }
}
