package br.com.brunocarvalhs.group.app.data.extensions

import br.com.brunocarvalhs.group.app.data.model.UserModel
import br.com.brunocarvalhs.group.app.domain.entities.UserEntities
import com.google.firebase.perf.metrics.AddTrace
import java.util.UUID

@AddTrace(name = "UserEntities.create", enabled = true)
fun UserEntities.Companion.create(
    id: String = UUID.randomUUID().toString(),
    likes: List<String> = emptyList(),
): UserEntities = UserModel(
    id = id,
    likes = likes,
)