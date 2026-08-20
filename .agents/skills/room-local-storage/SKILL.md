---
name: room-local-storage
description: Use this skill when adding local persistence with Room in the author's style. It defines conventions for the database, DAO, and entity trio — naming (<X>Database, <X>Dao, entity models), Flow-returning DAO methods for reactive data, KSP Room compiler configuration, and Hilt provisioning of the database instance. Follow it for any new local cache or offline feature.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Room
  - Database
  - DAO
  - Entity
  - KSP
  - Local storage
  - Flow
---

## Purpose

Standardize Room usage so local persistence follows one consistent shape and stays reactive.

## Core principles

- **Trio naming:** `<X>Database` (abstract class, `@Database`), `<X>Dao` (interface, `@Dao`), entity models (`@Entity`) in `app/data/local` (entities) alongside; DAOs in `app/data/local`.
- **Reactive reads:** DAO methods that read return `Flow<List<T>>` (or single `Flow<T>`) so the UI auto-updates from the ViewModel.
- **Writes** are `suspend fun` (or provide transaction helpers).
- **Compiler:** Room via KSP in the module build file:
  ```kotlin
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
  ```
- **Database instance provided through Hilt** (`@Provides @Singleton` in the module's companion — see `hilt-di-conventions`), e.g. `Room.databaseBuilder(context, XDatabase::class.java, "x.db").build()`.
- Data-access through the repository pattern — DAOs are injected into repository impls, never used by ViewModels directly.

## Code example (reference skeleton)

```kotlin
@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE groupId = :groupId ORDER BY timestamp")
    fun observeMessages(groupId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE groupId = :groupId")
    suspend fun clearMessages(groupId: String)
}
```

## Step-by-Step

1. Define the `@Entity` model (fields = persistence shape; may differ from domain model → map via `data/extensions`).
2. Create `<X>Dao` with Flow reads + suspend writes.
3. Create `<X>Database` exposing the DAO.
4. Provide the database via Hilt (`@Provides @Singleton` with `Room.databaseBuilder`).
5. Inject the DAO into the repository impl; bind via `@Binds` as usual.
6. Test DAO/database with Robolectric (`unit-testing-conventions`).

## Decision rules

- **Entity equals domain model?** → reuse the model directly; **differs?** → entity + mapper, keep domain clean.
- **UI observes changes?** → DAO returns `Flow`; **one-shot page load** → `suspend` single query acceptable.
- **Cross-table consistency?** → `@Transaction` methods on the DAO.
- **Schema changes** → bump `version` + migrations; `exportSchema = false` used in this codebase unless CI validates migrations.