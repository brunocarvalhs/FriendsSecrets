package br.com.brunocarvalhs.friendssecrets.commons.analytics

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsEventsTest {

    @Test
    fun `enum values should match expected strings`() {
        assertEquals("screen_view", AnalyticsEvents.VISUALIZATION.value)
        assertEquals("app_open", AnalyticsEvents.APP_OPEN.value)
        assertEquals("share", AnalyticsEvents.SHARE.value)
        assertEquals("select_content", AnalyticsEvents.CLICK.value)
        assertEquals("create_group", AnalyticsEvents.CREATE_GROUP.value)
        assertEquals("join_group", AnalyticsEvents.JOIN_GROUP.value)
        assertEquals("discover_secret_friend", AnalyticsEvents.DISCOVER_SECRET_FRIEND.value)
        assertEquals("submit_error_report", AnalyticsEvents.SUBMIT_ERROR_REPORT.value)
        assertEquals("change_theme", AnalyticsEvents.CHANGE_THEME.value)
        assertEquals("view_faq", AnalyticsEvents.VIEW_FAQ.value)
        assertEquals("view_group", AnalyticsEvents.VIEW_GROUP.value)
        assertEquals("enter_group", AnalyticsEvents.ENTER_GROUP.value)
        assertEquals("invite_friend", AnalyticsEvents.INVITE_FRIEND.value)
        assertEquals("initiate_draw", AnalyticsEvents.INITIATE_DRAW.value)
        assertEquals("view_members", AnalyticsEvents.VIEW_MEMBERS.value)
    }
}