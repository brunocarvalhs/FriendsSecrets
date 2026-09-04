package br.com.brunocarvalhs.core.notifications.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.brunocarvalhs.core.notifications.R
import timber.log.Timber

internal class GroupSyncNotifier(private val context: Context) {

    fun notifyDrawCompleted(groupToken: String, groupName: String) {
        notify(
            id = "$groupToken-draw".hashCode(),
            title = context.getString(R.string.group_sync_draw_completed_title),
            message = context.getString(R.string.group_sync_draw_completed_message, groupName)
        )
    }

    fun notifyNewMessage(groupToken: String, groupName: String) {
        notify(
            id = "$groupToken-message".hashCode(),
            title = context.getString(R.string.group_sync_new_message_title),
            message = context.getString(R.string.group_sync_new_message_message, groupName)
        )
    }

    private fun notify(id: Int, title: String, message: String) {
        ensureChannel()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
            Timber.w(e, "Missing POST_NOTIFICATIONS permission; skipping group sync notification")
        }
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.group_sync_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "group_sync_updates"
    }
}
