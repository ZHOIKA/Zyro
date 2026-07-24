# Implementation Plan - Fix GamesScreen Compose Preview Render Issue

The `GamesScreen` Composable fails to render in Android Studio Preview because it accesses `Prefs` during initialization (composition). `Prefs` uses `MMKV.defaultMMKV()`, which requires `MMKV.initialize(context)` to be called first. In a Preview environment, this initialization usually hasn't happened.

## Proposed Changes

### [feature_console_rpc]

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)
- Use `LocalInspectionMode.current` to detect if the Composable is running in a Preview.
- Guard the `Prefs` access in the `remember` block for `savedRpc`.
- Guard other `Prefs` accesses if necessary (though most are in interactive callbacks).

### [common/preference]

#### [MODIFY] [Prefs.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/common/preference/src/main/java/com/my/zyro/preference/Prefs.kt)
- Change `internal val kv = MMKV.defaultMMKV()` to use `lazy` initialization. This prevents the crash from happening just by referencing the `Prefs` object, and only crashes if `kv` is actually accessed when not initialized.

## Verification Plan

### Manual Verification
- Render the `GamesScreenPreview` in Android Studio and ensure it no longer throws `IllegalStateException`.
- Verify that other previews in `GamesScreen.kt` also work.
