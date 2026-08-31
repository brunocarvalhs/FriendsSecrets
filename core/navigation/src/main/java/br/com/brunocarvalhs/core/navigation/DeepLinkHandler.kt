package br.com.brunocarvalhs.core.navigation

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkHandler @Inject constructor() {

    @Volatile
    private var pendingJoinCode: String? = null

    fun handle(uri: Uri?) {
        if (uri?.scheme == SCHEME && uri.host == HOST_JOIN) {
            pendingJoinCode = uri.getQueryParameter(PARAM_CODE)
        }
    }

    fun consumePendingJoinCode(): String? {
        val code = pendingJoinCode
        pendingJoinCode = null
        return code
    }

    companion object {
        const val SCHEME = "friendssecrets"
        private const val HOST_JOIN = "join"
        private const val PARAM_CODE = "code"
    }
}
