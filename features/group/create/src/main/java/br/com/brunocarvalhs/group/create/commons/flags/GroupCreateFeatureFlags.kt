package br.com.brunocarvalhs.group.create.commons.flags

import br.com.brunocarvalhs.friendssecrets.domain.services.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GroupCreateFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isGroupCreateEnabled(): Boolean = service.validate(FEATURE_GROUP_CREATE, true)
    fun isContactsEnabled(): Boolean = service.validate(FEATURE_GROUP_CREATE_CONTACTS, true)
}

private const val FEATURE_GROUP_CREATE = "feature_group_create_enabled"
private const val FEATURE_GROUP_CREATE_CONTACTS = "feature_group_create_contacts_enabled"
