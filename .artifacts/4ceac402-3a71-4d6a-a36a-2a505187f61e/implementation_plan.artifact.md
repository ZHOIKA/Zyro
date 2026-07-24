# Implementation Plan - Fix GamesScreen Preview Render Issue

The `GamesScreen` Composable fails to render in Android Studio Preview because it accesses `Prefs` during initialization. `Prefs` uses `MMKV`, which requires `MMKV.initialize()` to be called. In a Preview environment, MMKV is not initialized, leading to an `IllegalStateException`.

## Proposed Changes

### [feature_console_rpc]

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)

- Import `LocalInspectionMode` from `androidx.compose.ui.platform`.
- Guard the `Prefs` access in `GamesScreen` using `LocalInspectionMode.current`. This will prevent `Prefs` from being accessed (and thus initialized) during Previews.

```kotlin
    val isPreview = LocalInspectionMode.current
    val savedRpc = remember {
        if (isPreview) "" else Prefs[Prefs.LAST_RUN_CONSOLE_RPC, ""]
    }
```

- Also guard other `Prefs` usages in the `SwitchBar` callback to ensure consistency, although they are less likely to be triggered in a static Preview.

## Verification Plan

### Manual Verification
- Render the `GamesScreenPreview` in Android Studio and verify that it no longer throws `IllegalStateException`.
- Verify that other previews in the same file also render correctly.
