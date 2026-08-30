package br.com.brunocarvalhs.group.details.app.data.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.receivers.ReminderReceiver
import br.com.brunocarvalhs.group.details.app.domain.services.GroupReminderService
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

internal class AlarmManagerGroupReminderService @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : GroupReminderService {

    override fun schedule(group: GroupModel): Boolean {
        val triggerAtMillis = triggerTimeFor(group.date) ?: return false
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return false

        runCatching {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent(group))
        }.onFailure {
            Timber.e(it, "Failed to schedule reminder for group ${group.id}")
            return false
        }

        return true
    }

    override fun cancel(group: GroupModel) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        alarmManager.cancel(pendingIntent(group))
    }

    private fun pendingIntent(group: GroupModel): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_GROUP_ID, group.id)
            putExtra(ReminderReceiver.EXTRA_GROUP_NAME, group.name)
        }
        return PendingIntent.getBroadcast(
            context,
            group.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun triggerTimeFor(date: String?): Long? {
        if (date.isNullOrBlank()) return null

        val parsedDate = runCatching {
            SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(date)
        }.getOrNull() ?: return null

        val calendar = Calendar.getInstance().apply {
            time = parsedDate
            set(Calendar.HOUR_OF_DAY, REMINDER_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis.takeIf { it > System.currentTimeMillis() }
    }

    private companion object {
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val REMINDER_HOUR = 9
    }
}
