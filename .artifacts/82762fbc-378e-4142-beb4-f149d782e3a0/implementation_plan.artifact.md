# Implementation Plan - Fix GamesScreen Render Issue

The `GamesScreen` Composable fails to render in Android Studio Preview because it accesses `Prefs` during initialization, which internally calls `MMKV.defaultMMKV()`. Since `MMKV` is not initialized in the Preview environment, it throws an `IllegalStateException`, leading to a `NoClassDefFoundError`.

## Proposed Changes

### common/preference

#### [MODIFY] [Prefs.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/common/preference/src/main/java/com/my/zyro/preference/Prefs.kt)

- Change `internal val kv = MMKV.defaultMMKV()` to use `lazy` initialization and wrap it in a `try-catch` block.
- Update `get`, `set`, and `remove` methods to handle the case where `kv` is null (e.g., in Previews or unit tests), returning default values or doing nothing as appropriate.

### feature_console_rpc

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)

- Although the fix in `Prefs.kt` should be sufficient, I will also wrap the `Prefs` access in `GamesScreen` with `LocalInspectionMode.current` check to be more explicit and avoid unnecessary overhead in Previews.

## Verification Plan

### Automated Tests
- Build the project to ensure no regressions.
- Run any existing unit tests for `Prefs`.

### Manual Verification
- Verify that `GamesScreenPreview` in `GamesScreen.kt` renders correctly in Android Studio.
