package br.com.brunocarvalhs.friendssecrets.commons.network

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class FirebaseCompatibilityConverter @Inject constructor() {

    /**
     * Converte recursivamente Any para JsonElement com suporte a compatibilidade NoSQL do Firebase.
     * Trata o caso onde o DTO espera uma lista (ex: members) mas o Firebase devolve um Mapa.
     */
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
            
            // Heurística 1: Chaves numéricas ("0", "1"...) sugerem uma lista corrompida pelo Firebase
            val isNumericIndexed = keys.isNotEmpty() && keys.all { it.toIntOrNull() != null }
            
            // Heurística 2: Se todos os valores são Objetos e temos chaves, 
            // pode ser um mapa usado como lista (ex: campo 'members')
            val valuesAreObjects = map.values.all { it is Map<*, *> }

            if (isNumericIndexed) {
                val sortedList = map.entries
                    .sortedBy { it.key.toString().toInt() }
                    .map { toJsonElement(it.value) }
                JsonArray(sortedList)
            } else if (valuesAreObjects && keys.isNotEmpty()) {
                // Converte os valores do mapa em uma lista para satisfazer DTOs que esperam List<T>
                JsonArray(map.values.map { toJsonElement(it) })
            } else {
                JsonObject(map.entries.associate { it.key.toString() to toJsonElement(it.value) })
            }
        }
        else -> JsonPrimitive(data.toString())
    }

    /**
     * Auxiliar para converter List para Array via reflexão, evitando ClassCastException na JVM
     */
    fun listToTypedArray(list: List<*>, arrayClass: Class<*>): Any {
        val componentType = arrayClass.componentType ?: Any::class.java
        val array = java.lang.reflect.Array.newInstance(componentType, list.size)
        for (i in list.indices) {
            java.lang.reflect.Array.set(array, i, list[i])
        }
        return array
    }
}
