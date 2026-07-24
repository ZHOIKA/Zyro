# Implementation Plan - Fix GamesScreen Preview Crash

The `GamesScreen` Composable fails to render in Android Studio Preview because it accesses `Prefs`, which depends on `MMKV`. `MMKV` must be initialized with a `Context` before use, but it's not initialized in the Preview environment, leading to an `IllegalStateException` and subsequently a `NoClassDefFoundError`.

## Proposed Changes

### [common-preference]

#### [MODIFY] [Prefs.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/common/preference/src/main/java/com/my/zyro/preference/Prefs.kt)
- Change the initialization of `kv` to be lazy. This prevents the `Prefs` object from crashing immediately upon being loaded in a Preview environment (unless `kv` is actually accessed).

```kotlin
    @PublishedApi
    internal val kv by lazy { MMKV.defaultMMKV() }
```

### [feature-console-rpc]

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)
- Guard the `Prefs` access in `GamesScreen` using `LocalInspectionMode.current`. This will prevent the lazy `kv` from being initialized in Preview mode.

```kotlin
    val isPreview = LocalInspectionMode.current
    val savedRpc = remember {
        if (isPreview) "" else Prefs[Prefs.LAST_RUN_CONSOLE_RPC, ""]
    }
```

## Verification Plan

### Automated Tests
- I will attempt to render the `GamesScreenPreview` using the `render_compose_preview` tool if available, or just rely on the fact that these guards are the standard way to fix such issues.

### Manual Verification
- The user can verify that the `GamesScreen` previews now render correctly in Android Studio.
