package br.com.brunocarvalhs.core.analytics.commons

enum class AnalyticsEvent(val value: String) {
    CLICK("click"),
    LONG_PRESS("long_press"),
    VIEW("view"),
    SUBMIT("submit"),
    NAVIGATE("navigate"),
    ERROR("error"),
    SEARCH("search"),

    // Group creation funnel
    GROUP_CREATE_STARTED("group_create_started"),
    GROUP_CREATE_MEMBERS_SELECTED("group_create_members_selected"),
    GROUP_CREATE_COMPLETED("group_create_completed"),
    GROUP_CREATE_FAILED("group_create_failed"),

    // Group join funnel
    GROUP_JOIN_STARTED("group_join_started"),
    GROUP_JOIN_SUBMITTED("group_join_submitted"),
    GROUP_JOIN_COMPLETED("group_join_completed"),
    GROUP_JOIN_FAILED("group_join_failed"),

    // Draw funnel
    DRAW_STARTED("draw_started"),
    DRAW_COMPLETED("draw_completed"),
    DRAW_REVEALED("draw_revealed"),

    // Invite funnel
    INVITE_SHARE_CODE("invite_share_code"),
    INVITE_SHARE_QR("invite_share_qr"),
    INVITE_SHARE_CARD("invite_share_card"),
}
