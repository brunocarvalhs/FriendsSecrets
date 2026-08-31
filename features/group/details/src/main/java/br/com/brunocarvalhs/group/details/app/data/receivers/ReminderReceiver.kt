package br.com.brunocarvalhs.group.details.app.data.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.brunocarvalhs.group.details.R
import timber.log.Timber

internal class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val groupName = intent.getStringExtra(EXTRA_GROUP_NAME).orEmpty()
        val groupId = intent.getStringExtra(EXTRA_GROUP_ID).orEmpty()

        ensureChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(context.getString(R.string.reminder_notification_title))
            .setContentText(
                context.getString(R.string.reminder_notification_message, groupName)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(groupId.hashCode(), notification)
        } catch (e: SecurityException) {
            Timber.w(e, "Missing POST_NOTIFICATIONS permission; skipping reminder notification")
        }
    }

    private fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.reminder_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val EXTRA_GROUP_ID = "extra_group_id"
        const val EXTRA_GROUP_NAME = "extra_group_name"
        private const val CHANNEL_ID = "group_draw_reminders"
    }
}
