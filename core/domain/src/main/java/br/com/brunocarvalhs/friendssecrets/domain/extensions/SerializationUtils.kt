package br.com.brunocarvalhs.friendssecrets.domain.extensions

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

fun JsonElement.toAny(): Any? {
    return when (this) {
        is JsonPrimitive -> {
            if (isString) contentOrNull
            else booleanOrNull ?: longOrNull ?: doubleOrNull
        }
        is JsonObject -> this.mapValues { it.value.toAny() }
        is JsonArray -> this.map { it.toAny() }
        JsonNull -> null
    }
}

fun JsonObject.toVanillaMap(): Map<String, Any?> {
    return this.mapValues { it.value.toAny() }
}
