# Plano de Implementação: Correção do Tema AMOLED (High Contrast)

O tema AMOLED (High Contrast) não está sendo aplicado corretamente porque a variável `isHighContrastModeEnabled` é ignorada na função principal `ZyroTheme`. Este plano visa corrigir a implementação para garantir que o fundo e as superfícies fiquem pretas puras quando o modo estiver ativado.

## Mudanças Propostas

### Tema (UI Theme)

#### [MODIFY] [Color.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Color.kt)
*   Alterar `ZyroBlack` de `0xFF0F0F0F` para `0xFF000000` para garantir o efeito AMOLED real (pixels desligados).

#### [MODIFY] [Theme.kt](file:///C:/Users/20241INF0014/StudioProjects/Zyro/theme/src/main/java/com/my/zyro/ui/theme/Theme.kt)
*   Refatorar a função `ZyroTheme` para aplicar a lógica de contraste.
*   Criar uma lógica que modifica o `ColorScheme` (seja o dinâmico do Android ou o baseado no Monet) para usar `Color.Black` no fundo e nas superfícies quando `darkTheme && isHighContrastModeEnabled` forem verdadeiros.
*   Garantir que o `SystemUiController` atualize a cor da barra de navegação com o novo fundo preto.

## Plano de Verificação

### Verificação Manual
1. Abrir as configurações do App.
2. Ir para as configurações de Tema Escuro.
3. Ativar o Tema Escuro.
4. Ativar a opção "AMOLED".
5. Verificar se o fundo do aplicativo muda para preto puro (#000000).
6. Verificar se a barra de navegação do sistema também fica preta.
