# Guia de Atualização Manual: Tema AMOLED e Console RPC

Como não tenho permissão para editar os arquivos diretamente, preparei os blocos de código exatos que você deve aplicar.

## 1. Tema AMOLED

### Arquivo: [Color.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Color.kt)
**Localizar:**
```kotlin
val ZyroBlack = Color(0xFF0F0F0F)       // True black background
```
**Substituir por:**
```kotlin
val ZyroBlack = Color(0xFF000000)       // True black background (AMOLED)
```

---

### Arquivo: [Theme.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Theme.kt)
**Substituir o corpo da função `ZyroTheme` (linhas 96-150 aproximadamente) por:**

```kotlin
@Composable
fun ZyroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
    isDynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = view.context.findWindow()

    if (isDynamicColorEnabled) {
        // Material You dynamic colors (Android 12+)
        var colorScheme = if (darkTheme) dynamicDarkColorScheme(context)
                          else dynamicLightColorScheme(context)

        // Aplica Preto Puro se AMOLED estiver ativado
        if (darkTheme && isHighContrastModeEnabled) {
            colorScheme = colorScheme.copy(
                background = Color.Black,
                surface = Color.Black,
                surfaceVariant = Color.Black,
                surfaceContainer = Color.Black,
                surfaceContainerHigh = Color.Black,
                surfaceContainerHighest = Color.Black
            )
        }

        window?.let {
            WindowCompat.getInsetsController(it, view)?.isAppearanceLightStatusBars = !darkTheme
        }
        rememberSystemUiController(window).apply {
            setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
            setNavigationBarColor(color = colorScheme.background, darkIcons = !darkTheme)
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    } else {
        // Monet color science
        val seedColor = Color(LocalSeedColor.current)
        val paletteStyle = palettesMap[LocalPaletteStyleIndex.current] ?: PaletteStyle.Vibrant
        val tonalPalettes = seedColor.toTonalPalettes(paletteStyle)

        CompositionLocalProvider(LocalTonalPalettes provides tonalPalettes) {
            var colorScheme = dynamicColorScheme(isLight = !darkTheme)

            if (darkTheme && isHighContrastModeEnabled) {
                colorScheme = colorScheme.copy(
                    background = Color.Black,
                    surface = Color.Black,
                    surfaceVariant = Color.Black,
                    surfaceContainer = Color.Black
                )
            }

            window?.let {
                WindowCompat.getInsetsController(it, view)?.isAppearanceLightStatusBars = !darkTheme
            }
            rememberSystemUiController(window).apply {
                setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
                setNavigationBarColor(color = colorScheme.background, darkIcons = !darkTheme)
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = AppTypography,
                content = content
            )
        }
    }
}
```

---

## 2. Console RPC

### Arquivo: [Constants.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/feature_rpc_base/src/main/java/com/my/zyro/feature_rpc_base/Constants.kt)
**Atualizar a `data class PlatformOption` e a lista `CONSOLE_PLATFORMS`:**

```kotlin
    data class PlatformOption(
        val displayName: String,
        val iconUrl: String,
        val platformId: String // Adicionar este campo
    )

    val CONSOLE_PLATFORMS = listOf(
        PlatformOption(displayName = "PlayStation 5", iconUrl = "https://i.imgur.com/ps5_icon.png", platformId = "ps5"),
        PlatformOption(displayName = "PlayStation 4", iconUrl = "https://i.imgur.com/ps4_icon.png", platformId = "ps4"),
        PlatformOption(displayName = "Xbox Series X", iconUrl = "https://i.imgur.com/xbox_series_x_icon.png", platformId = "xbox"),
        PlatformOption(displayName = "Xbox One", iconUrl = "https://i.imgur.com/xbox_one_icon.png", platformId = "xbox"),
        PlatformOption(displayName = "Nintendo Switch", iconUrl = "https://i.imgur.com/switch_icon.png", platformId = "switch"),
        PlatformOption(displayName = "PC", iconUrl = "https://i.imgur.com/pc_icon.png", platformId = "pc"),
        PlatformOption(displayName = "Mobile", iconUrl = "https://i.imgur.com/mobile_icon.png", platformId = "mobile"),
    )
```

---

### Arquivo: [GamesScreen.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)
**Na lógica do `onPlatformSelected` dentro do `PlatformPickerDialog` (por volta da linha 105):**

```kotlin
            onPlatformSelected = { platform ->
                showPlatformPicker = false
                pendingGame = null
                selected = currentGame.game_title
                val string = Json.encodeToString(
                    RpcConfig(
                        name = platform.displayName,
                        details = currentGame.game_title,
                        state = "Playing ${currentGame.game_title}", // Adicionado
                        timestampsStart = System.currentTimeMillis().toString(),
                        status = "dnd",
                        largeImg = currentGame.large_image ?: "",
                        smallImg = platform.iconUrl,
                        smallText = platform.displayName, // Adicionado
                        type = "0",
                        platform = platform.platformId, // Usar platformId aqui
                    )
                )
                // ... resto do código ...
            }
```
