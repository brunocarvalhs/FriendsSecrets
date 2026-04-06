package br.com.brunocarvalhs.group.details.commons.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

inline fun <reified T : Any> navTypeSerializer() = object : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(URLDecoder.decode(value, StandardCharsets.UTF_8.name()))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: T): String {
        return URLEncoder.encode(Json.encodeToString(value), StandardCharsets.UTF_8.name())
    }
}
