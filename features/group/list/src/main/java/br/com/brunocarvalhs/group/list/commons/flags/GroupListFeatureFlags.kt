package br.com.brunocarvalhs.group.list.commons.flags

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GroupListFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isListEnabled(): Boolean = service.validate(FEATURE_GROUP_LIST, true)
    fun isBannerEnabled(): Boolean = service.validate(FEATURE_GROUP_LIST_BANNER, true)
}

private const val FEATURE_GROUP_LIST = "feature_group_list_enabled"
private const val FEATURE_GROUP_LIST_BANNER = "feature_group_list_banner_enabled"
