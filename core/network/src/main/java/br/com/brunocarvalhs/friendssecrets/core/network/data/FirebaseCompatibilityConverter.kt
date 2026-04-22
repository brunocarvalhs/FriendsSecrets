package br.com.brunocarvalhs.friendssecrets.core.network.data

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class FirebaseCompatibilityConverter @Inject constructor() {

    fun toJsonElement(data: Any?): JsonElement = when (data) {
        null -> JsonNull
        is JsonElement -> data
        is Boolean -> JsonPrimitive(data)
        is Number -> JsonPrimitive(data)
        is String -> JsonPrimitive(data)
        is Iterable<*> -> JsonArray(data.map { toJsonElement(it) })
        is Map<*, *> -> {
            val map = data as Map<*, *>
            val keys = map.keys.mapNotNull { it?.toString() }
            
            val isNumericIndexed = keys.isNotEmpty() && keys.all { it.toIntOrNull() != null }
            val valuesAreObjects = map.values.all { it is Map<*, *> }

            if (isNumericIndexed) {
                val sortedList = map.entries
                    .sortedBy { it.key.toString().toInt() }
                    .map { toJsonElement(it.value) }
                JsonArray(sortedList)
            } else if (valuesAreObjects && keys.isNotEmpty()) {
                JsonArray(map.values.map { toJsonElement(it) })
            } else {
                JsonObject(map.entries.associate { it.key.toString() to toJsonElement(it.value) })
            }
        }
        else -> JsonPrimitive(data.toString())
    }

    fun listToTypedArray(list: List<*>, arrayClass: Class<*>): Any {
        val componentType = arrayClass.componentType ?: Any::class.java
        val array = java.lang.reflect.Array.newInstance(componentType, list.size)
        for (i in list.indices) {
            java.lang.reflect.Array.set(array, i, list[i])
        }
        return array
    }
}