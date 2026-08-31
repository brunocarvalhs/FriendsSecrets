package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.content.Context
import android.content.Intent
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.group.details.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ShareWishlistUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val deviceService: DeviceService,
) {
    suspend operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        val deviceId = deviceService.getDeviceId()
        val self = group.members.firstOrNull { it.isCurrentDevice(deviceId) }
            ?: throw MemberNotFoundException()
        val likes = self.likes.filter { it.isNotBlank() }
        if (likes.isEmpty()) throw EmptyWishlistException()

        val message = context.getString(
            R.string.share_wishlist_message,
            group.name,
            likes.joinToString(separator = "\n") { "• $it" }
        )

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val shareIntent = Intent.createChooser(
            sendIntent,
            context.getString(R.string.share_wishlist_title)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(shareIntent)
    }

    private fun UserModel.isCurrentDevice(deviceId: String): Boolean {
        return id == deviceId || phoneNumber == deviceId
    }

    internal class MemberNotFoundException : Exception("Current device is not a member of this group")
    internal class EmptyWishlistException : Exception("There are no wishlist items to share")

    companion object {
        private const val TYPE = "text/plain"
    }
}
