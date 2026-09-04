package br.com.brunocarvalhs.group.list.app.presentation

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsUserProperty
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.navigation.DeepLinkHandler
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.group.list.commons.flags.GroupListFeatureFlags
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
internal class GroupListViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
    private val analyticsService: AnalyticsService,
    private val deepLinkHandler: DeepLinkHandler,
    private val featureFlags: GroupListFeatureFlags,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GroupListUiState(
            isCreateGroupEnabled = featureFlags.isCreateGroupEnabled(),
            isJoinGroupEnabled = featureFlags.isJoinGroupEnabled()
        )
    )
    val uiState: StateFlow<GroupListUiState> = _uiState.asStateFlow()

    init {
        deepLinkHandler.consumePendingJoinCode()?.let { code -> groupToEnter(code) }
    }

    @AddTrace(name = "GroupListViewModel.handleEvent", enabled = true)
    fun handleEvent(intent: GroupListIntent) {
        when (intent) {
            GroupListIntent.FetchGroups -> fetchGroups()
            GroupListIntent.JoinGroupStarted -> joinGroupStarted()
            is GroupListIntent.GroupToEnter -> groupToEnter(intent.token)
            is GroupListIntent.OnSearchQueryChange -> searchGroups(intent.query)
            is GroupListIntent.OnTagSelected -> filterGroups(intent.tag)
        }
    }

    @AddTrace(name = "GroupListViewModel.joinGroupStarted", enabled = true)
    private fun joinGroupStarted() {
        analyticsService.logEvent(name = AnalyticsEvent.GROUP_JOIN_STARTED)
    }

    @AddTrace(name = "GroupListViewModel.searchGroups", enabled = true)
    private fun searchGroups(query: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SEARCH,
            params = mapOf(
                AnalyticsParam.ACTION to "search_groups",
                AnalyticsParam.PARAM to query
            )
        )
        _uiState.update { it.copy(searchQuery = query) }
    }

    @AddTrace(name = "GroupListViewModel.filterGroups", enabled = true)
    private fun filterGroups(tag: GroupFilterTag) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "filter_groups",
                AnalyticsParam.PARAM to tag.name
            )
        )
        _uiState.update { it.copy(selectedTag = tag) }
    }

    @AddTrace(name = "GroupListViewModel.groupToEnter", enabled = true)
    private fun groupToEnter(token: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "group_to_enter",
                AnalyticsParam.PARAM to token
            )
        )
        analyticsService.logEvent(
            name = AnalyticsEvent.GROUP_JOIN_SUBMITTED,
            params = mapOf(AnalyticsParam.PARAM to token)
        )
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            groupByTokenUseCase.invoke(token)
                .onSuccess {
                    analyticsService.logEvent(
                        name = AnalyticsEvent.GROUP_JOIN_COMPLETED,
                        params = mapOf(AnalyticsParam.PARAM to token)
                    )
                    analyticsService.setUserProperty(
                        AnalyticsUserProperty.HAS_JOINED_GROUP.value,
                        "true"
                    )
                    fetchGroups()
                }
                .onFailure {
                    analyticsService.logEvent(
                        name = AnalyticsEvent.GROUP_JOIN_FAILED,
                        params = mapOf(AnalyticsParam.PARAM to token)
                    )
                    error(it)
                }
        }
    }

    @AddTrace(name = "GroupListViewModel.fetchGroups", enabled = true)
    private fun fetchGroups() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "fetch_groups"
            )
        )
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            groupListUseCase.invoke()
                .onSuccess(::success)
                .onFailure(::error)
        }
    }

    @AddTrace(name = "GroupListViewModel.success", enabled = true)
    private fun success(result: List<GroupModel>) {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "success",
                AnalyticsParam.PARAM to result.toString()
            )
        )
        _uiState.update {
            it.copy(
                isLoading = false,
                list = result,
                errorMessage = null
            )
        }
    }

    @AddTrace(name = "GroupListViewModel.error", enabled = true)
    private fun error(t: Throwable) {
        analyticsService.logEvent(
            name = AnalyticsEvent.ERROR,
            params = mapOf(
                AnalyticsParam.ACTION to "error",
                AnalyticsParam.PARAM to t.message
            )
        )
        Timber.e(t)
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = t.message.orEmpty()
            )
        }
    }
}
