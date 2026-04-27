package br.com.brunocarvalhs.group.draw.commons.flags

import br.com.brunocarvalhs.core.remote.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GroupDrawFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isDrawEnabled(): Boolean = service.validate(FEATURE_GROUP_DRAW, true)
    fun isResultEnabled(): Boolean = service.validate(FEATURE_GROUP_DRAW_RESULT, true)
}

private const val FEATURE_GROUP_DRAW = "feature_group_draw_enabled"
private const val FEATURE_GROUP_DRAW_RESULT = "feature_group_draw_result_enabled"
