package br.com.brunocarvalhs.friendssecrets.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics

enum class AnalyticsEvents(val value: String) {
    // Eventos padrão do GA4
    VISUALIZATION(FirebaseAnalytics.Event.SCREEN_VIEW),
    APP_OPEN(FirebaseAnalytics.Event.APP_OPEN),
    SHARE(FirebaseAnalytics.Event.SHARE),
    CLICK(FirebaseAnalytics.Event.SELECT_CONTENT),

    // Eventos personalizados específicos do aplicativo
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