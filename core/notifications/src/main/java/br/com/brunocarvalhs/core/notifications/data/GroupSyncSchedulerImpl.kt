package br.com.brunocarvalhs.core.notifications.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.brunocarvalhs.core.notifications.domain.GroupSyncScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class GroupSyncSchedulerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : GroupSyncScheduler {

    override fun schedule() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<GroupSyncWorker>(
            SYNC_INTERVAL_MINUTES, TimeUnit.MINUTES
        ).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private companion object {
        const val WORK_NAME = "group_sync_work"
        const val SYNC_INTERVAL_MINUTES = 30L
    }
}
