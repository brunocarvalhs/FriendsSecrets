package br.com.brunocarvalhs.friendssecrets.commons.analytics

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsParamsTest {

    @Test
    fun `enum values should match expected strings`() {
        assertEquals("screen_name", AnalyticsParams.SCREEN_NAME.value)
        assertEquals("item_id", AnalyticsParams.ITEM_ID.value)
        assertEquals("item_name", AnalyticsParams.ITEM_NAME.value)
        assertEquals("content_type", AnalyticsParams.CONTENT_TYPE.value)
        assertEquals("value", AnalyticsParams.VALUE.value)
        assertEquals("user_action", AnalyticsParams.USER_ACTION.value)
        assertEquals("group_id", AnalyticsParams.GROUP_ID.value)
        assertEquals("member_id", AnalyticsParams.MEMBER_ID.value)
        assertEquals("error_message", AnalyticsParams.ERROR_MESSAGE.value)
        assertEquals("faq_section", AnalyticsParams.FAQ_SECTION.value)
        assertEquals("user_id", AnalyticsParams.USER_ID.value)
        assertEquals("group_name", AnalyticsParams.GROUP_NAME.value)
        assertEquals("member_name", AnalyticsParams.MEMBER_NAME.value)
        assertEquals("theme", AnalyticsParams.THEME.value)
        assertEquals("report_type", AnalyticsParams.REPORT_TYPE.value)
        assertEquals("faq_question", AnalyticsParams.FAQ_QUESTION.value)
    }
}