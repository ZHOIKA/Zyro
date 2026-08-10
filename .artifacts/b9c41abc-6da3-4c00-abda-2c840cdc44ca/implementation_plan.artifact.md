# Plano de Implementação: Correções de Tema AMOLED e Console RPC

Este plano aborda dois problemas: o tema AMOLED que não aplica o fundo preto puro e o Console RPC que não exibe as informações corretamente no Discord.

## Mudanças Propostas

### 1. Correção do Tema AMOLED

#### [MODIFY] [Color.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Color.kt)
*   Alterar `ZyroBlack` para `0xFF000000` (Preto Puro).

#### [MODIFY] [Theme.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Theme.kt)
*   Atualizar `ZyroTheme` para forçar o fundo e as superfícies para preto puro quando `isHighContrastModeEnabled` estiver ativado no modo escuro.
*   Garantir que as cores dinâmicas (Material You) também respeitem o modo AMOLED.

---

### 2. Correção do Console RPC

O Console RPC não está funcionando como esperado devido a metadados incompletos e falta de mapeamento de plataforma para o Discord.

#### [MODIFY] [Constants.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/feature_rpc_base/src/main/java/com/my/zyro/feature_rpc_base/Constants.kt)
*   Adicionar campo `platformId` à data class `PlatformOption`.
*   Atualizar a lista `CONSOLE_PLATFORMS` com os IDs reconhecidos pelo Discord spoofing (`ps5`, `ps4`, `xbox`, `switch`, `pc`, `mobile`).

#### [MODIFY] [GamesScreen.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/feature_console_rpc/src/main/java/com/my/zyro/feature_console_rpc/GamesScreen.kt)
*   Melhorar a construção do `RpcConfig`:
    *   Usar `platform.platformId` para o campo `platform` do RPC.
    *   Definir `smallText` com o nome da plataforma.
    *   Definir `state` como "Playing" (ou similar) para preencher o perfil do Discord.
*   Persistir o jogo selecionado no `Prefs` para que a UI reflita o estado atual ao reabrir a tela.

#### [MODIFY] [CustomRpcService.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/feature_rpc_base/src/main/java/com/my/zyro/feature_rpc_base/services/CustomRpcService.kt)
*   Garantir que a limpeza de estado (`resetState`) ocorra corretamente antes de cada nova transmissão.
*   Verificar se o `applicationId` customizado está sendo aplicado corretamente.

## Plano de Verificação

### Verificação Manual (AMOLED)
1. Ativar modo escuro e AMOLED nas configurações.
2. Validar se o fundo é `#000000`.

### Verificação Manual (Console RPC)
1. Selecionar um jogo (ex: "Elden Ring") e uma plataforma (ex: "PlayStation 5").
2. Ativar o RPC.
3. Verificar no Discord se aparece:
    *   Nome: **PlayStation 5**
    *   Detalhes: **Elden Ring**
    *   Estado: **Playing**
    *   Ícones: Logo do PS5 (pequeno) e arte do jogo (grande).
