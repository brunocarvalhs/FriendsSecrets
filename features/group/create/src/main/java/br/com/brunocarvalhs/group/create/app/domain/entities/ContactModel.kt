package br.com.brunocarvalhs.group.create.app.domain.entities

data class ContactModel(
    val id: String,
    val photoUrl: String? = null,
    val name: String,
    val phoneNumber: String,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "photoUrl" to photoUrl,
            "name" to name,
            "phoneNumber" to phoneNumber
        )
    }
}