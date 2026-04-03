package br.com.brunocarvalhs.group.commons.providers.datasources

interface GroupDataSource {
    suspend fun save(group: Map<String, Any?>)
    suspend fun findById(groupId: String): Map<String, Any>?
    suspend fun delete(groupId: String)
    suspend fun listByTokens(tokens: List<String>): List<Map<String, Any>>
    suspend fun findByToken(token: String): Map<String, Any>?
    suspend fun runTransaction(block: suspend (transaction: GroupTransaction) -> Unit)

    interface GroupTransaction {
        suspend fun get(groupId: String): Map<String, Any>?
        fun update(groupId: String, data: Map<String, Any?>)
    }
}