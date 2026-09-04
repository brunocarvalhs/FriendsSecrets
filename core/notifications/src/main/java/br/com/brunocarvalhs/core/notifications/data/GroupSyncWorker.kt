package br.com.brunocarvalhs.core.notifications.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.notifications.data.model.GroupSyncDTO
import br.com.brunocarvalhs.core.notifications.data.model.GroupSyncState
import br.com.brunocarvalhs.core.notifications.di.GroupSyncEntryPoint
import br.com.brunocarvalhs.storage.domain.StorageService
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.tasks.await
import timber.log.Timber

internal class GroupSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val entryPoint by lazy {
        EntryPointAccessors.fromApplication(applicationContext, GroupSyncEntryPoint::class.java)
    }
    private val networkService: NetworkService by lazy { entryPoint.networkService() }
    private val storageService: StorageService by lazy { entryPoint.storageService() }
    private val notifier by lazy { GroupSyncNotifier(applicationContext) }

    override suspend fun doWork(): Result = runCatching {
        val tokens = storageService.load(GroupModel.COLLECTION_NAME, Array<String>::class)
            ?.toList().orEmpty()

        val groups = if (tokens.isEmpty()) {
            emptyList()
        } else {
            networkService.make(
                request = NetworkRequest(
                    endpoint = GroupModel.COLLECTION_NAME,
                    query = mapOf(GroupModel.TOKEN to tokens),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupSyncDTO>::class
            )?.toList().orEmpty()
        }

        groups.forEach { group -> checkGroup(group) }

        Result.success()
    }.getOrElse { error ->
        Timber.e(error, "Group sync check failed")
        Result.retry()
    }

    private suspend fun checkGroup(group: GroupSyncDTO) {
        val stateKey = "$STATE_KEY_PREFIX${group.token}"
        val previousState = storageService.load(stateKey, GroupSyncState::class)
        val hasDraw = group.draws.isNotEmpty()
        val lastMessageTimestamp = fetchLastMessageTimestamp(group.token)

        if (shouldNotifyDrawCompleted(previousState, hasDraw)) {
            notifier.notifyDrawCompleted(group.token, group.name)
        }
        if (shouldNotifyNewMessage(previousState, lastMessageTimestamp)) {
            notifier.notifyNewMessage(group.token, group.name)
        }

        storageService.save(
            stateKey,
            GroupSyncState(hasDraw = hasDraw, lastMessageTimestamp = lastMessageTimestamp)
        )
    }

    private suspend fun fetchLastMessageTimestamp(groupToken: String): Long = runCatching {
        val snapshot = FirebaseDatabase.getInstance()
            .getReference(CHATS_PATH)
            .child(groupToken)
            .orderByChild(TIMESTAMP_FIELD)
            .limitToLast(1)
            .get()
            .await()

        snapshot.children.firstOrNull()?.child(TIMESTAMP_FIELD)?.getValue(Long::class.java) ?: 0L
    }.getOrElse { error ->
        Timber.w(error, "Could not fetch last message timestamp for group %s", groupToken)
        0L
    }

    private companion object {
        const val STATE_KEY_PREFIX = "group_sync_state_"
        const val CHATS_PATH = "chats"
        const val TIMESTAMP_FIELD = "ts"
    }
}

internal fun shouldNotifyDrawCompleted(previousState: GroupSyncState?, hasDraw: Boolean): Boolean =
    previousState != null && hasDraw && !previousState.hasDraw

internal fun shouldNotifyNewMessage(previousState: GroupSyncState?, lastMessageTimestamp: Long): Boolean =
    previousState != null && lastMessageTimestamp > previousState.lastMessageTimestamp
