package br.com.brunocarvalhs.group.list.app.domain.entities

data class GroupModel(
    val id: String,
    val name: String,
    val description: String,
    val token: String,
    val members: List<UserModel>
) {
    companion object {
        fun fromMap(map: Map<String, Any>): GroupModel {
            return GroupModel(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                token = map["token"] as? String ?: "",
                members = (map["members"] as? List<Map<String, Any>>)?.map { UserModel.fromMap(it) } ?: emptyList(),
            )
        }
    }
}
