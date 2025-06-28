package br.com.brunocarvalhs.friendssecrets.common.analytics

enum class AnalyticsEvents(val value: String) {
    VISUALIZATION("screen_view"),
    APP_OPEN("app_open"),
    SHARE("share"),
    CLICK("select_content"),
    CREATE_GROUP("create_group"),
    JOIN_GROUP("join_group"),
    DISCOVER_SECRET_FRIEND("discover_secret_friend"),
    SUBMIT_ERROR_REPORT("submit_error_report"),
    CHANGE_THEME("change_theme"),
    VIEW_FAQ("view_faq"),
    VIEW_GROUP("view_group"),
    ENTER_GROUP("enter_group"),
    INVITE_FRIEND("invite_friend"),
    INITIATE_DRAW("initiate_draw"),
    VIEW_MEMBERS("view_members")
}