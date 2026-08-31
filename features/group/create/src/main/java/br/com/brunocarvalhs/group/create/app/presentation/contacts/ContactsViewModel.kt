package br.com.brunocarvalhs.group.create.app.presentation.contacts

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.core.navigation.routers.ContactsRouter
import br.com.brunocarvalhs.core.navigation.routers.EditFormsGraph
import br.com.brunocarvalhs.group.create.app.domain.useCases.GetContactsUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class ContactsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContactsUseCase: GetContactsUseCase,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val args = savedStateHandle.toRoute<ContactsRouter>(ContactsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        value = ContactsUiState(
            isEditing = args.group?.members?.isNotEmpty() ?: false,
            members = args.group?.members.orEmpty(),
        )
    )
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        if (args.group == null) {
            analyticsService.logEvent(name = AnalyticsEvent.GROUP_CREATE_STARTED)
        }
        handleIntent(ContactsIntent.LoadContacts)
    }

    @AddTrace(name = "ContactsViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: ContactsIntent) {
        when (intent) {
            is ContactsIntent.LoadContacts -> loadContacts()
            is ContactsIntent.SearchContacts -> searchContacts(intent.query)
            is ContactsIntent.ToggleMember -> toggleMember(intent.contact)
            is ContactsIntent.RemoveMember -> removeMember(intent.contact)
            is ContactsIntent.Next -> next(intent.callback)
        }
    }

    @AddTrace(name = "ContactsViewModel.loadContacts", enabled = true)
    private fun loadContacts() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getContactsUseCase().onSuccess { contacts ->
                _uiState.update { currentState ->
                    currentState.copy(
                        contacts = contacts,
                        filteredContacts = filterContacts(contacts, currentState.searchQuery),
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    @AddTrace(name = "ContactsViewModel.searchContacts", enabled = true)
    private fun searchContacts(query: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "search_contacts",
                AnalyticsParam.PARAM to query
            )
        )
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredContacts = filterContacts(currentState.contacts, query)
            )
        }
    }

    @AddTrace(name = "ContactsViewModel.toggleMember", enabled = true)
    private fun toggleMember(contact: UserModel) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "toggle_member",
                AnalyticsParam.PARAM to contact.name
            )
        )
        _uiState.update { currentState ->
            val isAlreadySelected = currentState.members.any {
                (contact.phoneNumber.isNotBlank() && it.phoneNumber == contact.phoneNumber) ||
                        (it.id == contact.id)
            }

            if (isAlreadySelected) {
                currentState.copy(
                    members = currentState.members.filterNot { 
                        (contact.phoneNumber.isNotBlank() && it.phoneNumber == contact.phoneNumber) || 
                        it.id == contact.id 
                    }
                )
            } else {
                currentState.copy(members = currentState.members + contact)
            }
        }
    }

    @AddTrace(name = "ContactsViewModel.removeMember", enabled = true)
    private fun removeMember(contact: UserModel) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "remove_member",
                AnalyticsParam.PARAM to contact.name
            )
        )
        _uiState.update { currentState ->
            currentState.copy(
                members = currentState.members.filterNot { it.id == contact.id }
            )
        }
    }

    @AddTrace(name = "ContactsViewModel.filterContacts", enabled = true)
    private fun filterContacts(contacts: List<UserModel>, query: String): List<UserModel> {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "filter_contacts",
                AnalyticsParam.PARAM to query
            )
        )
        if (query.isBlank()) return contacts
        return contacts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.phoneNumber.contains(query)
        }
    }

    @AddTrace(name = "ContactsViewModel.next", enabled = true)
    private fun next(callback: (Any) -> Unit) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "next"
            )
        )
        val members = _uiState.value.members
        
        if (members.isEmpty()) {
            _uiState.update { it.copy(error = "Selecione ao menos um membro") }
            return
        }
        
        if (members.size < MIN_GROUP_MEMBERS) {
            _uiState.update { it.copy(error = "O grupo precisa de no mínimo 3 membros") }
            return
        }

        args.group?.let {
            callback(
                EditFormsGraph(
                    group = it.copy(members = members)
                )
            )
        } ?: run {
            analyticsService.logEvent(
                name = AnalyticsEvent.GROUP_CREATE_MEMBERS_SELECTED,
                params = mapOf(AnalyticsParam.PARAM to members.size.toString())
            )
            callback(
                FormsRouter(
                    members = members,
                )
            )
        }
    }

    private companion object {
        const val MIN_GROUP_MEMBERS = 3
    }
}
