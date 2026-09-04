---
name: feature-flags
description: Use this skill when adding feature flags to an Android app backed by remote configuration. It defines the author's pattern — a per-feature FeatureFlags @Singleton class injected with a FeatureFlagService, private constants named FEATURE_X with value "feature_x_enabled", and boolean accessor methods delegating to service.validate(KEY, default). Follow it to gate features, A/B experiments, or rollout safely.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Feature flags
  - Remote Config
  - FeatureFlagService
  - validate
  - Rollout
  - Singleton
---

## Purpose

Standardize feature-flag plumbing: one `FeatureFlags` wrapper per feature, keys as private constants, defaults passed at call site.

## Core principles

- **One class per feature:** `<X>FeatureFlags`, `@Singleton`, `@Inject constructor(private val service: FeatureFlagService)`.
- **Key constants private to the class:** `private const val FEATURE_X = "feature_x_enabled"` (snake_case, `_enabled` suffix).
- **One accessor per flag:** `fun isXEnabled(): Boolean = service.validate(FEATURE_X, true)`.
- **Defaults are explicit** at the validate call — the same default the feature ships with.
- **No flag logic leaks** outside the `FeatureFlags` class; callers invoke `isXEnabled()` only.
- The `FeatureFlagService` abstraction (Remote Config backed) lives in `core:remote` (`domain/FeatureFlagService`, impl `config/FeatureFlagsManager`).

## Code example (reference)

```kotlin
@Singleton
internal class ChatFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isChatEnabled(): Boolean = service.validate(FEATURE_CHAT, true)
    fun isSendMessageEnabled(): Boolean = service.validate(FEATURE_CHAT_SEND_MESSAGE, true)
}

private const val FEATURE_CHAT = "feature_chat_enabled"
private const val FEATURE_CHAT_SEND_MESSAGE = "feature_chat_send_message_enabled"
```

## Step-by-Step

1. Create `commons/flags/<X>FeatureFlags.kt` in the feature.
2. Add `private const val` per flag with `feature_<name>_enabled` key.
3. Add `fun is<Capability>Enabled(): Boolean = service.validate(KEY, default)`.
4. Consume in ViewModel/UI: inject the flag class, branch with an early return or conditional rendering.
5. Register the associated keys in Remote Config with matching defaults.

## Decision rules

- **New gated behavior?** → add accessor to the existing `<X>FeatureFlags`; only create a new class for a new feature module.
- **Default on/off?** → choose the safe default for current rollout; opt-in experiments default `false`.
- **Anyone else reading keys?** → no — only the flag class; prevents key-string drift.
- **Flag only affects UI?** → read in Screen; **affects logic?** → read in ViewModel/usecase.
- **Constant vs accessor naming:** constant `FEATURE_<NAME>`; accessor `is<Name>Enabled`.