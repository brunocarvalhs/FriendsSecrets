import com.google.firebase.analytics.FirebaseAnalytics

enum class AnalyticsParameter(val value: String) {

    // Básicos
    ACHIEVEMENT_ID(FirebaseAnalytics.Param.ACHIEVEMENT_ID),
    AD_FORMAT(FirebaseAnalytics.Param.AD_FORMAT),
    AD_PLATFORM(FirebaseAnalytics.Param.AD_PLATFORM),
    AD_SOURCE(FirebaseAnalytics.Param.AD_SOURCE),
    AD_UNIT_NAME(FirebaseAnalytics.Param.AD_UNIT_NAME),

    // Conteúdo / Item
    ITEM_ID(FirebaseAnalytics.Param.ITEM_ID),
    ITEM_NAME(FirebaseAnalytics.Param.ITEM_NAME),
    ITEM_CATEGORY(FirebaseAnalytics.Param.ITEM_CATEGORY),
    ITEM_CATEGORY2(FirebaseAnalytics.Param.ITEM_CATEGORY2),
    ITEM_CATEGORY3(FirebaseAnalytics.Param.ITEM_CATEGORY3),
    ITEM_CATEGORY4(FirebaseAnalytics.Param.ITEM_CATEGORY4),
    ITEM_CATEGORY5(FirebaseAnalytics.Param.ITEM_CATEGORY5),
    ITEM_BRAND(FirebaseAnalytics.Param.ITEM_BRAND),
    ITEM_VARIANT(FirebaseAnalytics.Param.ITEM_VARIANT),
    ITEM_LIST_ID(FirebaseAnalytics.Param.ITEM_LIST_ID),
    ITEM_LIST_NAME(FirebaseAnalytics.Param.ITEM_LIST_NAME),
    ITEMS(FirebaseAnalytics.Param.ITEMS),

    // Conteúdo genérico
    CONTENT(FirebaseAnalytics.Param.CONTENT),
    CONTENT_TYPE(FirebaseAnalytics.Param.CONTENT_TYPE),

    // Busca / navegação
    SEARCH_TERM(FirebaseAnalytics.Param.SEARCH_TERM),

    // Transação / e-commerce
    CURRENCY(FirebaseAnalytics.Param.CURRENCY),
    VALUE(FirebaseAnalytics.Param.VALUE),
    PRICE(FirebaseAnalytics.Param.PRICE),
    QUANTITY(FirebaseAnalytics.Param.QUANTITY),
    TAX(FirebaseAnalytics.Param.TAX),
    SHIPPING(FirebaseAnalytics.Param.SHIPPING),
    TRANSACTION_ID(FirebaseAnalytics.Param.TRANSACTION_ID),
    PAYMENT_TYPE(FirebaseAnalytics.Param.PAYMENT_TYPE),
    AFFILIATION(FirebaseAnalytics.Param.AFFILIATION),
    COUPON(FirebaseAnalytics.Param.COUPON),
    DISCOUNT(FirebaseAnalytics.Param.DISCOUNT),

    // Produto alternativo (novo naming)
    PRODUCT_ID(FirebaseAnalytics.Param.PRODUCT_ID),
    PRODUCT_NAME(FirebaseAnalytics.Param.PRODUCT_NAME),

    // Localização
    LOCATION(FirebaseAnalytics.Param.LOCATION),
    LOCATION_ID(FirebaseAnalytics.Param.LOCATION_ID),

    // Usuário / comportamento
    METHOD(FirebaseAnalytics.Param.METHOD),
    SUCCESS(FirebaseAnalytics.Param.SUCCESS),
    SCORE(FirebaseAnalytics.Param.SCORE),

    // Game / app
    LEVEL(FirebaseAnalytics.Param.LEVEL),
    LEVEL_NAME(FirebaseAnalytics.Param.LEVEL_NAME),
    CHARACTER(FirebaseAnalytics.Param.CHARACTER),

    // Viagem
    TRAVEL_CLASS(FirebaseAnalytics.Param.TRAVEL_CLASS),
    NUMBER_OF_NIGHTS(FirebaseAnalytics.Param.NUMBER_OF_NIGHTS),
    NUMBER_OF_PASSENGERS(FirebaseAnalytics.Param.NUMBER_OF_PASSENGERS),
    NUMBER_OF_ROOMS(FirebaseAnalytics.Param.NUMBER_OF_ROOMS),
    DESTINATION(FirebaseAnalytics.Param.DESTINATION),
    ORIGIN(FirebaseAnalytics.Param.ORIGIN),
    FLIGHT_NUMBER(FirebaseAnalytics.Param.FLIGHT_NUMBER),

    // Datas
    START_DATE(FirebaseAnalytics.Param.START_DATE),
    END_DATE(FirebaseAnalytics.Param.END_DATE),

    // Sessão
    EXTEND_SESSION(FirebaseAnalytics.Param.EXTEND_SESSION),

    // Campanha / marketing
    CAMPAIGN(FirebaseAnalytics.Param.CAMPAIGN),
    SOURCE(FirebaseAnalytics.Param.SOURCE),
    MEDIUM(FirebaseAnalytics.Param.MEDIUM),
    TERM(FirebaseAnalytics.Param.TERM),
    CONTENT_CAMPAIGN(FirebaseAnalytics.Param.CONTENT), // cuidado: duplicado semântico
    ACLID(FirebaseAnalytics.Param.ACLID),
    CP1(FirebaseAnalytics.Param.CP1),
    CAMPAIGN_ID(FirebaseAnalytics.Param.CAMPAIGN_ID),
    SOURCE_PLATFORM(FirebaseAnalytics.Param.SOURCE_PLATFORM),
    CREATIVE_FORMAT(FirebaseAnalytics.Param.CREATIVE_FORMAT),
    MARKETING_TACTIC(FirebaseAnalytics.Param.MARKETING_TACTIC),

    // Promoção
    PROMOTION_ID(FirebaseAnalytics.Param.PROMOTION_ID),
    PROMOTION_NAME(FirebaseAnalytics.Param.PROMOTION_NAME),
    CREATIVE_NAME(FirebaseAnalytics.Param.CREATIVE_NAME),
    CREATIVE_SLOT(FirebaseAnalytics.Param.CREATIVE_SLOT),

    // UI / tela
    SCREEN_NAME(FirebaseAnalytics.Param.SCREEN_NAME),
    SCREEN_CLASS(FirebaseAnalytics.Param.SCREEN_CLASS),

    // Outros
    INDEX(FirebaseAnalytics.Param.INDEX),
    SHIPPING_TIER(FirebaseAnalytics.Param.SHIPPING_TIER),
    FREE_TRIAL(FirebaseAnalytics.Param.FREE_TRIAL),
    PRICE_IS_DISCOUNTED(FirebaseAnalytics.Param.PRICE_IS_DISCOUNTED),
    SUBSCRIPTION(FirebaseAnalytics.Param.SUBSCRIPTION)
}