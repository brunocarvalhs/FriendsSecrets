package br.com.brunocarvalhs.friendssecrets.domain.entities

interface GroupEntities {
    val id: String
    val token: String
    val name: String
    val description: String?
    val members: Map<String, String>
    val isDraw: Boolean
    val draws: Map<String, String>
    val isOwner: Boolean

    fun toMap(): Map<*, *>

    fun toCopy(
        token: String = this.token,
        name: String = this.name,
        description: String? = this.description,
        members: Map<String, String> = this.members,
        isDraw: Boolean = this.isDraw,
        draws: Map<String, String> = this.draws,
        isOwner: Boolean = this.isOwner,
    ): GroupEntities

    companion object {
        const val COLLECTION_NAME = "groups"
        const val COLLECTION_NAME_ADMINS = "${COLLECTION_NAME}_admins"

        const val ID = "id"
        const val TOKEN = "token"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val MEMBERS = "members"
        const val DRAWS = "draws"
        const val IS_DRAW = "isDraw"
        const val IS_OWNER = "isOwner"
    }
}