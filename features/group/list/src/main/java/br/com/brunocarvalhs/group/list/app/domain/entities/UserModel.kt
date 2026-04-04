package br.com.brunocarvalhs.group.list.app.domain.entities

data class UserModel(
    val name: String
) {
    companion object {
        fun fromMap(map: Map<String, Any>): UserModel {
            return UserModel(
                name = map["name"] as String,
            )
        }
    }
}
