package br.com.brunocarvalhs.friendssecrets.core.network.domain

data class NetworkRequest(
    val endpoint: String,
    val payload: Map<String, Any?>? = null,
    val headers: Map<String, String>? = null,
    val query: Map<String, Any>? = null,
    val method: NetworkService.Method
)
