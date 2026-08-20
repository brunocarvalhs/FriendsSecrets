---
name: analytics-tracking
description: Use this skill when instrumenting an Android app with analytics in the author's style. It defines the AnalyticsService contract, the AnalyticsEvent enum vocabulary (VIEW, CLICK, SUBMIT, SEARCH, ERROR, LONG_PRESS, NAVIGATE), the AnalyticsParam key set (ACTION, PARAM, SCREEN, ...), and the mandatory rules — every user action logs an event with ACTION set to the snake_case method name. Follow it so app behavior is uniformly measurable and analyzable.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Analytics
  - AnalyticsService
  - AnalyticsEvent
  - AnalyticsParam
  - Events
  - Firebase Analytics
  - User tracking
---

## Purpose

Standardize analytics instrumentation: a minimal service contract, a fixed event vocabulary, and a discipline of logging every user action.

## Core principles

- **Service contract (core module):**
  ```kotlin
  interface AnalyticsService {
      fun logEvent(name: AnalyticsEvent, params: Map<AnalyticsParam, Any?> = emptyMap())
      fun setUserProperty(name: String, value: String)
      fun setUserId(userId: String)
  }
  ```
- **`AnalyticsEvent` enum — fixed vocabulary:**
  `CLICK("click")`, `LONG_PRESS("long_press")`, `VIEW("view")`, `SUBMIT("submit")`, `NAVIGATE("navigate")`, `ERROR("error")`, `SEARCH("search")`.
- **`AnalyticsParam` keys:** `ACTION`, `PARAM`, `SCREEN`, plus any context key — event payload built as `mapOf(AnalyticsParam.ACTION to "snake_case_action", AnalyticsParam.PARAM to value)`.
- **Mandatory rule — every user action logs:** each ViewModel method starts with `analyticsService.logEvent(...)` using the matching event type and `ACTION to "method_name"` (snake_case, matches the method).
- **Screen tracking:** at the app entry point, observe `navController.currentBackStackEntryFlow` and log `VIEW` with `AnalyticsParam.SCREEN to destination.route`.
- **User identity:** `setUserId` / `setUserProperty` called once (device-id wired in an app-level initializer).

## Code example (reference)

```kotlin
// ViewModel, top of every action method
@AddTrace(name = "GroupListViewModel.searchGroups", enabled = true)
private fun searchGroups(query: String) {
    analyticsService.logEvent(
        name = AnalyticsEvent.SEARCH,
        params = mapOf(
            AnalyticsParam.ACTION to "search_groups",
            AnalyticsParam.PARAM to query
        )
    )
    _uiState.update { it.copy(searchQuery = query) }
}
```

```kotlin
// activity: screen tracking
LaunchedEffect(navController) {
    navController.currentBackStackEntryFlow.collect { backStackEntry ->
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(AnalyticsParam.SCREEN to backStackEntry.destination.route)
        )
    }
}
```

## Step-by-Step

1. Add a method to a ViewModel → log at the top: event type from the vocabulary + `ACTION to "<snake_case_method_name>"`.
2. Include the triggering value when meaningful: `PARAM to <value>`.
3. Navigation/screen open → `VIEW` with `SCREEN` param (handled centrally at app level).
4. User taps list item / selects option → `CLICK`; search input → `SEARCH`; send/create → `SUBMIT`; failures → `ERROR`.
5. Do not log in domain/data layers — tracking lives at the presentation boundary.

## Decision rules

- **Event type for this action?** → `VIEW` (screen/open), `CLICK` (selection), `SUBMIT` (create/send), `SEARCH` (query), `ERROR` (failure), `NAVIGATE` (route change), `LONG_PRESS` (context menu).
- **What goes in PARAM?** → the user-provided value or result payload; keep it small.
- **Unknown intent?** → use `VIEW` + `ACTION to "method_name"` as the safe default.
- **PII?** → never log raw credentials; log ids/values only where privacy permits.