---
name: unit-testing-conventions
description: Use this skill when writing unit tests for an Android/Kotlin project in the author's style. It defines the stack (JUnit4, MockK, kotlinx-coroutines-test runTest, Robolectric), naming (backtick sentences), structure (Given/When/Then comments), verification (coEvery/coVerify), and layer coverage philosophy — repositories, use cases, mappers, initializers, and serializers are tested; ViewModels are intentionally not unit-tested. Follow it for any test file so tests stay fast, deterministic, and consistent.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Unit tests
  - JUnit4
  - MockK
  - runTest
  - Robolectric
  - coVerify
  - Given When Then
---

## Purpose

Standardize unit tests: stack, naming, structure, and which layers get tested.

## Core principles

- **Stack:** JUnit4 (`org.junit.Test`), MockK (`mockk()`, `coEvery`, `coVerify`), `kotlinx-coroutines-test` (`runTest`), Robolectric for Android-dependent code.
- **Test naming:** backtick sentences (`sendMessage should call network and service`).
- **Structure:** `// Given` / `// When` / `// Then` comments inside each test.
- **Setup:** fields for mocks (`private val chatService: ChatService = mockk()`), `lateinit` for the subject, `@Before fun setup()` wiring subject with mocks.
- **Verification:** `coVerify { service.method(args) }` for suspend calls; assertion via JUnit `assert*`.
- **Suspending code** under `runTest { ... }`.
- **Coverage philosophy (author's style):**
  - Tested: repository impls, use cases, mappers/extensions, initializers, routers/serializers, DAOs (Robolectric).
  - **Not tested: ViewModels** (business logic lives in use cases; ViewModel stays thin), instrumented UI tests limited to essentials.
- **Package mirroring:** test tree mirrors main tree (`src/test/java/...` same package).

## Code example (reference)

```kotlin
class ChatRepositoryImplTest {

    private val chatService: ChatService = mockk()
    private lateinit var repository: ChatRepositoryImpl

    @Before
    fun setup() {
        repository = ChatRepositoryImpl(chatService)
    }

    @Test
    fun `sendMessage should call network and service`() = runTest {
        // Given
        val groupId = "group1"
        val message = MessageModel(id = "msg1", text = "Hello", groupId = groupId)
        coEvery { chatService.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = repository.sendMessage(groupId, message)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.sendMessage(groupId, message) }
    }

    @Test
    fun `getMessages should return flow from service`() = runTest {
        // Given
        val groupId = "group1"
        val expectedFlow = flowOf(emptyList<MessageModel>())
        coEvery { chatService.getMessages(groupId) } returns expectedFlow

        // When
        val result = repository.getMessages(groupId)

        // Then
        assertTrue(result === expectedFlow)
        coVerify { chatService.getMessages(groupId) }
    }
}
```

## Step-by-Step

1. Mirror package under `src/test/java`.
2. Declare mocks as fields; subject as `lateinit`; wire in `@Before`.
3. Write tests as backtick sentences with Given/When/Then comments.
4. `coEvery` stubs (Given), call subject (When), `assert` + `coVerify` (Then).
5. Run with the module's test task (`testDebugUnitTest`).

## Decision rules

- **Suspend subject?** → `runTest { }` + `coEvery`/`coVerify`.
- **Android framework class needed (Context, Room)?** → Robolectric.
- **Same behavior for MockK and Mockito both present?** → prefer MockK (codebase standard).
- **New business logic?** → put it in a use case, test the use case — don't add ViewModel tests (author's philosophy).
- **Mapper/extensions** → pure unit tests, no mocks needed.