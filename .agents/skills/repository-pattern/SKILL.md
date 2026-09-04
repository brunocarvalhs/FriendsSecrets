---
name: repository-pattern
description: Use this skill when implementing the data-access layer in the author's Clean Architecture style. It defines the contract split — domain repository/services interfaces versus data-layer implementations — plus DTO modeling, mapper extensions, domain exceptions, and Result/Flow return contracts. Follow it when adding persistence, network, or Firebase-backed data sources so the domain stays framework-free and testable.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Repository pattern
  - Data layer
  - DTO
  - Mapper
  - Domain exceptions
  - Result
  - Flow
  - Firebase
---

## Purpose

Standardize how data is modeled and accessed: domain contracts define the "what", data implementations define the "how".

## Core principles

- **Contracts live in domain** (framework-free):
  - `app/domain/repository/<X>Repository.kt` — aggregate data access for a feature.
  - `app/domain/services/<X>Service.kt` — lower-level service contract (e.g. Firebase manager).
- **Implementations live in data:**
  - `app/data/repository/<X>RepositoryImpl.kt`.
  - `app/data/services/<X...>Manager.kt` (e.g. `FirebaseRealtimeManager`).
- **DTOs** (`app/data/model`) represent external/local payloads; **domain models** (`core:domain/model` or `app/domain/model`) represent business objects — convert via extension functions/mappers in `app/data/extensions`.
- **Domain exceptions** (`app/data/exceptions`) express failure conditions by name: `GroupAlreadyExistException`, `GroupNotFoundException`.
- **Return contracts:** `Result<T>` for one-shot operations; `Flow<T>` for reactive data (Room/Firebase listeners).
- **Detection & conversion at the data layer:** errors from Firebase/DB are wrapped into domain exceptions; `Timber` logs at the data edge.
- **Binding:** `@Binds interface → Impl` in the Hilt module (`hilt-di-conventions`).

## Code example (reference skeleton)

```kotlin
// domain contract
interface ChatRepository {
    suspend fun getMessages(groupId: String): Flow<List<MessageModel>>
    suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit>
    suspend fun clearMessages(groupId: String): Result<Unit>
}

// data implementation (delegates to a service + converts)
class ChatRepositoryImpl(private val chatService: ChatService) : ChatRepository {
    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> =
        chatService.getMessages(groupId).map { it.toMessageModel() } // mapper extension

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> =
        chatService.sendMessage(groupId, message)
}
```

## Step-by-Step

1. Define the contract interface in `domain` (methods returning `Result`/`Flow`, domain models only).
2. Implement in `data`; inject services/DAO/data sources via constructor.
3. Map DTO ↔ domain with extension functions in `data/extensions` (test them).
4. Convert data-layer failures into named domain exceptions.
5. Bind interface→impl in the module's Hilt file.
6. Test impl with mocked service (`unit-testing-conventions`).

## Decision rules

- **Who owns the data source?** → service interface (Firebase/DB); **who composes sources?** → repository.
- **One-shot call?** → `Result<T>`; **changing data?** → `Flow<T>`.
- **External payload shape differs from domain?** → DTO + mapper; never leak DTO into domain.
- **Failure must be actionable?** → dedicated exception type in `data/exceptions` instead of generic `Exception`.
- **Repository vs use case:** repository = data access; use case = business rule orchestrating repositories — keep them separate.