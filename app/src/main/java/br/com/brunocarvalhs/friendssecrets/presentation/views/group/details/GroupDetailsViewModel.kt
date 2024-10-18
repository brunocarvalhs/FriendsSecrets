package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupDrawUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupReadUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupDetailsViewModel(
    private val groupReadUseCase: GroupReadUseCase,
    private val groupDrawUseCase: GroupDrawUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GroupDetailsUiState> =
        MutableStateFlow(GroupDetailsUiState.Loading)

    val uiState: StateFlow<GroupDetailsUiState> =
        _uiState.asStateFlow()

    fun eventIntent(intent: GroupDetailsIntent) {
        when (intent) {
            is GroupDetailsIntent.FetchGroup -> fetchGroup(intent.groupId)
            is GroupDetailsIntent.DrawMembers -> drawMembers(intent.group)
            is GroupDetailsIntent.ShareMember -> shareMember(
                context = intent.context,
                member = intent.member,
                secret = intent.secret,
                token = intent.token
            )
        }
    }

    private fun shareMember(context: Context, member: String, secret: String, token: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Olá $member, baixando nosso aplicativo, código do grupo: $token e seu amigo secreto é: $secret"
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

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val repository = GroupRepositoryImpl()
                    val storage = StorageService()
                    val groupReadUseCase = GroupReadUseCase(
                        groupRepository = repository,
                        storage = storage
                    )
                    val groupDrawUseCase = GroupDrawUseCase(
                        groupRepository = repository
                    )
                    GroupDetailsViewModel(
                        groupReadUseCase = groupReadUseCase,
                        groupDrawUseCase = groupDrawUseCase
                    )
                }
            }
    }
}