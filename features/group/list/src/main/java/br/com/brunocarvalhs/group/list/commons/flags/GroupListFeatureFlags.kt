package br.com.brunocarvalhs.group.list.commons.flags

import br.com.brunocarvalhs.core.remote.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GroupListFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isCreateGroupEnabled(): Boolean = service.validate(FEATURE_CREATE_GROUP, true)
    fun isJoinGroupEnabled(): Boolean = service.validate(FEATURE_JOIN_GROUP, true)
}

private const val FEATURE_CREATE_GROUP = "home_is_create_group_enabled"
private const val FEATURE_JOIN_GROUP = "home_is_join_group_enabled"
