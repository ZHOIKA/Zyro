# Implementation Plan - Unit Tests for GamesScreen.kt

The goal is to provide a set of unit tests for the `GamesScreen` feature, specifically focusing on the `GamesViewModel` logic and potentially the UI components if an appropriate testing environment is set up.

## User Review Required

> [!IMPORTANT]
> The project currently lacks a mocking library (like MockK or Mockito). I will implement fakes for the UseCase and Repository to avoid adding new dependencies, unless you prefer me to add a mocking library to `libs.versions.toml`.

> [!NOTE]
> `GamesViewModel` directly depends on `android.content.Context` to fetch string resources. For pure unit tests (JUnit), this usually requires Robolectric or mocking the Context. I will propose using a fake context or Robolectric if available.

## Proposed Changes

### [feature_console_rpc]

#### [NEW] [GamesViewModelTest.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/test/java/com/my/zyro/feature_console_rpc/GamesViewModelTest.kt)
Create a unit test class for `GamesViewModel`.
- Test initial state (Loading).
- Test successful data fetch (Success).
- Test error handling (Error).
- Test search functionality.
- Test UI events (CloseSearchBar, OpenSearchBar, Search, TryAgain).

#### [NEW] [Fakes.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/test/java/com/my/zyro/feature_console_rpc/Fakes.kt)
Implement fake versions of `GetGamesUseCase` and `Context` (if needed) to support the ViewModel tests.

## Verification Plan

### Automated Tests
- Run the newly created unit tests using Gradle:
  `./gradlew :feature_console_rpc:testDebugUnitTest`

### Manual Verification
- N/A for unit tests, but will ensure the build still passes.
