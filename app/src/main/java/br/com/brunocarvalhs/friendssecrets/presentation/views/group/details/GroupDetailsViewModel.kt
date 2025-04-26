package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupDeleteUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupDrawUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupEditUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupExitUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val groupReadUseCase: GroupReadUseCase,
    private val groupEditUseCase: GroupEditUseCase,
    private val groupDrawUseCase: GroupDrawUseCase,
    private val groupExitUseCase: GroupExitUseCase,
    private val groupDeleteUseCase: GroupDeleteUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GroupDetailsUiState> =
        MutableStateFlow(GroupDetailsUiState.Loading)

    val uiState: StateFlow<GroupDetailsUiState> =
        _uiState.asStateFlow()

    fun eventIntent(intent: GroupDetailsIntent) {
        when (intent) {
            is GroupDetailsIntent.FetchGroup -> fetchGroup(groupId = intent.groupId)
            is GroupDetailsIntent.DrawMembers -> drawMembers(group = intent.group)
            is GroupDetailsIntent.ExitGroup -> exitGroup(groupId = intent.groupId)
            is GroupDetailsIntent.DeleteGroup -> deleteGroup(groupId = intent.groupId)
            is GroupDetailsIntent.ShareMember -> shareMember(
                context = intent.context,
                member = intent.member,
                secret = intent.secret,
                token = intent.token
            )

            is GroupDetailsIntent.EditMember -> editMember(
                entities = intent.group,
                member = intent.participant,
                likes = intent.likes
            )

            is GroupDetailsIntent.RemoveMember -> removeMember(
                entities = intent.group,
                member = intent.participant
            )

            is GroupDetailsIntent.ShareGroup -> shareGroup(intent.context, intent.group)
        }
    }

    private fun shareGroup(context: Context, group: GroupEntities) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(
                    R.string.group_details_share_member_simple,
                    group.name,
                    group.token,
                )
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    private fun removeMember(
        entities: GroupEntities,
        member: String
    ) {
        viewModelScope.launch {
            val group = entities.toCopy(
                members = entities.members.filter { !it.equals(member) }
            )
            groupEditUseCase.invoke(group)
                .onSuccess {
                    _uiState.value = GroupDetailsUiState.Success(it)
                }.onFailure {
                    _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
                }
        }
    }

    private fun editMember(
        entities: GroupEntities,
        member: String,
        likes: List<String>
    ) {
        viewModelScope.launch {
            groupEditUseCase.invoke(
                entities.toCopy(
                    members = HashMap(entities.members).apply {
                        put(member, likes.joinToString(separator = "|"))
                    }
                )
            ).onSuccess {
                _uiState.value = GroupDetailsUiState.Success(it)
            }.onFailure {
                _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    private fun deleteGroup(groupId: String) {
        _uiState.value = GroupDetailsUiState.Loading
        viewModelScope.launch {
            groupDeleteUseCase.invoke(groupId).onSuccess {
                _uiState.value = GroupDetailsUiState.Exit
            }.onFailure {
                _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    private fun exitGroup(groupId: String) {
        _uiState.value = GroupDetailsUiState.Loading
        viewModelScope.launch {
            groupExitUseCase.invoke(groupId).onSuccess {
                _uiState.value = GroupDetailsUiState.Exit
            }.onFailure {
                _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    private fun shareMember(context: Context, member: String, secret: String, token: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(
                    R.string.group_details_share_member,
                    member,
                    token,
                    secret,
                    BuildConfig.APPLICATION_ID,
                    context.getString(R.string.home_drop_menu_item_text_join_a_group)
                )
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    private fun drawMembers(group: GroupEntities) {
        _uiState.value = GroupDetailsUiState.Loading
        viewModelScope.launch {
            delay(timeMillis = 3000)
            groupDrawUseCase.invoke(group).onSuccess {
                _uiState.value = GroupDetailsUiState.Draw
            }.onFailure {
                _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    private fun fetchGroup(groupId: String) {
        _uiState.value = GroupDetailsUiState.Loading
        viewModelScope.launch {
            delay(timeMillis = 1000)
            groupReadUseCase.invoke(groupId).onSuccess { group ->
                _uiState.value = GroupDetailsUiState.Success(group)
            }.onFailure {
                _uiState.value = GroupDetailsUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }
}