package br.com.brunocarvalhs.friendssecrets.data.extensions

import kotlinx.serialization.json.*

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
