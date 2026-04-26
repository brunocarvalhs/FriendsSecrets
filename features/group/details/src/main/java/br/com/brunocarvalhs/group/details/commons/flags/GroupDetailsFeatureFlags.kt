package br.com.brunocarvalhs.group.details.commons.flags

import br.com.brunocarvalhs.core.remote.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GroupDetailsFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isGroupDetailsEnabled(): Boolean = service.validate(FEATURE_GROUP_DETAILS, true)
    fun isMembersEnabled(): Boolean = service.validate(FEATURE_GROUP_DETAILS_MEMBERS, true)
}

private const val FEATURE_GROUP_DETAILS = "feature_group_details_enabled"
private const val FEATURE_GROUP_DETAILS_MEMBERS = "feature_group_details_members_enabled"
