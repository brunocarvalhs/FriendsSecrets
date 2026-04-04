package br.com.brunocarvalhs.friendssecrets.commons.flags

import br.com.brunocarvalhs.group.create.commons.providers.GroupCreateToggles

private val flags by lazy { FeatureFlagsManager.getInstance() }

object FlagsManager: GroupCreateToggles {

}