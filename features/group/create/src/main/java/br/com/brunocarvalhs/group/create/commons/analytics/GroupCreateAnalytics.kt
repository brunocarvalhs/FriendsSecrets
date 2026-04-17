package br.com.brunocarvalhs.group.create.commons.analytics

internal interface GroupCreateAnalytics {
    fun trackFormsScreenView()
    fun trackContactsScreenView()
    fun trackEditFormScreenView()
    fun trackCreateGroup(success: Boolean)
    fun trackAddMember()
    fun trackSelectContact()
}

