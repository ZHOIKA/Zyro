# Implementation Plan - Fix Console RPC Not Appearing

The user reports that the Console RPC is still not appearing on Discord. This is likely due to lifecycle issues in `DiscordWebSocketImpl` and potential synchronization issues in `GamesScreen`.

## User Review Required

> [!IMPORTANT]
> The `DiscordWebSocketImpl` currently cancels its own coroutine scope when closed, which prevents any subsequent connection attempts from succeeding until the app is restarted. I will change this to only cancel active connection jobs.

## Proposed Changes

### gateway

#### [MODIFY] [DiscordWebSocketImpl.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/gateway/src/main/java/zyro/gateway/DiscordWebSocketImpl.kt)
- Fix `coroutineContext` to use a stable `SupervisorJob` instead of creating a new one on every access.
- Update `close()` to use `coroutineContext.cancelChildren()` instead of `this.cancel()`. This keeps the singleton scope alive for future connection attempts.
- Improve `connect()` to properly manage `connectionJob` and avoid race conditions during the handshake.
- Ensure `isConnectedToAccount` is reset correctly.

### data

#### [MODIFY] [ZyroRPC.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/data/src/main/java/com/my/zyro/data/rpc/ZyroRPC.kt)
- Add a setter or refresh mechanism for `applicationIdNumber` to ensure it uses the latest value from `Prefs` when a new RPC is built.
- Add more robust logging for the presence payload to aid future debugging.

### feature_console_rpc

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/Enzo Andrade/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)
- Refine the `SwitchBar` toggle logic: only update the `isConsoleRpcRunning` state if the service is successfully started or stopped.
- Ensure that the initial state of the switch correctly reflects the service status and has access to the `savedRpc`.

## Verification Plan

### Automated Tests
- Build the project to verify no compilation errors.

### Manual Verification
- Deploy the app and test the Console RPC feature.
- Verify that toggling the RPC on/off multiple times works correctly without requiring an app restart.
- Check logs for "Transmitting presence update" to confirm the payload is being sent.
