package br.com.brunocarvalhs.core.analytics.commons

import com.google.firebase.analytics.FirebaseAnalytics

enum class AnalyticsEvent(val value: String) {

    AD_IMPRESSION(FirebaseAnalytics.Event.AD_IMPRESSION),
    ADD_PAYMENT_INFO(FirebaseAnalytics.Event.ADD_PAYMENT_INFO),
    ADD_TO_CART(FirebaseAnalytics.Event.ADD_TO_CART),
    ADD_TO_WISHLIST(FirebaseAnalytics.Event.ADD_TO_WISHLIST),
    APP_OPEN(FirebaseAnalytics.Event.APP_OPEN),
    BEGIN_CHECKOUT(FirebaseAnalytics.Event.BEGIN_CHECKOUT),
    CAMPAIGN_DETAILS(FirebaseAnalytics.Event.CAMPAIGN_DETAILS),
    GENERATE_LEAD(FirebaseAnalytics.Event.GENERATE_LEAD),
    JOIN_GROUP(FirebaseAnalytics.Event.JOIN_GROUP),
    LEVEL_END(FirebaseAnalytics.Event.LEVEL_END),
    LEVEL_START(FirebaseAnalytics.Event.LEVEL_START),
    LEVEL_UP(FirebaseAnalytics.Event.LEVEL_UP),
    LOGIN(FirebaseAnalytics.Event.LOGIN),
    POST_SCORE(FirebaseAnalytics.Event.POST_SCORE),
    SEARCH(FirebaseAnalytics.Event.SEARCH),
    SELECT_CONTENT(FirebaseAnalytics.Event.SELECT_CONTENT),
    SHARE(FirebaseAnalytics.Event.SHARE),
    SIGN_UP(FirebaseAnalytics.Event.SIGN_UP),
    SPEND_VIRTUAL_CURRENCY(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY),
    TUTORIAL_BEGIN(FirebaseAnalytics.Event.TUTORIAL_BEGIN),
    TUTORIAL_COMPLETE(FirebaseAnalytics.Event.TUTORIAL_COMPLETE),
    UNLOCK_ACHIEVEMENT(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT),
    VIEW_ITEM(FirebaseAnalytics.Event.VIEW_ITEM),
    VIEW_ITEM_LIST(FirebaseAnalytics.Event.VIEW_ITEM_LIST),
    VIEW_SEARCH_RESULTS(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS),
    EARN_VIRTUAL_CURRENCY(FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY),
    SCREEN_VIEW(FirebaseAnalytics.Event.SCREEN_VIEW),
    REMOVE_FROM_CART(FirebaseAnalytics.Event.REMOVE_FROM_CART),
    ADD_SHIPPING_INFO(FirebaseAnalytics.Event.ADD_SHIPPING_INFO),
    IN_APP_PURCHASE(FirebaseAnalytics.Event.IN_APP_PURCHASE),
    PURCHASE(FirebaseAnalytics.Event.PURCHASE),
    REFUND(FirebaseAnalytics.Event.REFUND),
    SELECT_ITEM(FirebaseAnalytics.Event.SELECT_ITEM),
    SELECT_PROMOTION(FirebaseAnalytics.Event.SELECT_PROMOTION),
    VIEW_CART(FirebaseAnalytics.Event.VIEW_CART),
    VIEW_PROMOTION(FirebaseAnalytics.Event.VIEW_PROMOTION)
}
