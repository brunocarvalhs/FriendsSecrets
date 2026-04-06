package br.com.brunocarvalhs.friendssecrets.domain.entities

import java.util.UUID
import kotlin.random.Random

abstract class GroupEntities {

    open val id: String = UUID.randomUUID().toString()
    open val token: String = token()
    open val name: String = ""
    open val description: String? = null
    open val date: String? = null
    open val minPrice: Double? = null
    open val maxPrice: Double? = null
    open val type: String? = null
    open val members: List<UserEntities> = emptyList()
    open val draws: Map<String, String> = emptyMap()
    open val isOwner: Boolean = false
    open val photo: String? = null

    open fun toMap(): Map<String, Any?> = mapOf(
        ID to id,
        TOKEN to token,
        NAME to name,
        DESCRIPTION to description,
        DATE to date,
        MIN_PRICE to minPrice,
        MAX_PRICE to maxPrice,
        TYPE to type,
        MEMBERS to members,
        DRAWS to draws,
        IS_OWNER to isOwner,
        PHOTO to photo
    )

    abstract fun toCopy(
        token: String = this.token,
        name: String = this.name,
        description: String? = this.description,
        date: String? = this.date,
        minPrice: Double? = this.minPrice,
        maxPrice: Double? = this.maxPrice,
        type: String? = this.type,
        members: List<UserEntities> = this.members,
        draws: Map<String, String> = this.draws,
        isOwner: Boolean = this.isOwner,
        photo: String? = this.photo,
    ): GroupEntities

    open fun token(size: Int = 8): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..size)
            .map { charPool[Random.nextInt(charPool.size)] }
            .joinToString("")
    }

    companion object {
        const val COLLECTION_NAME = "groups"
        const val COLLECTION_NAME_ADMINS = "${COLLECTION_NAME}_admins"

        const val ID = "id"
        const val TOKEN = "token"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val MIN_PRICE = "min_price"
        const val MAX_PRICE = "max_price"
        const val TYPE = "type"
        const val MEMBERS = "members"
        const val DRAWS = "draws"
        const val IS_OWNER = "is_owner"
        const val PHOTO = "photo_base64"
    }
}