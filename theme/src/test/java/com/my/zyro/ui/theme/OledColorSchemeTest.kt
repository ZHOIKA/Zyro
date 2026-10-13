package com.my.zyro.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test

class OledColorSchemeTest {
    @Test
    fun oledUsesBlackSurfacesAndPreservesAccent() {
        val original = darkColorScheme(primary = Color.Cyan, primaryContainer = Color.Blue)
        val oled = original.withOled(true)
        assertEquals(Color.Black, oled.background)
        assertEquals(Color.Black, oled.surface)
        assertEquals(Color.Black, oled.surfaceDim)
        assertEquals(Color.Black, oled.surfaceContainerLowest)
        assertEquals(Color.Transparent, oled.surfaceTint)
        assertEquals(original.primary, oled.primary)
        assertEquals(original.primaryContainer, oled.primaryContainer)
        assertTrue(oled.onSurface.red > 0.9f)
    }

    @Test
    fun disablingOledPreservesLightAndDarkSchemes() {
        val light = lightColorScheme()
        val dark = darkColorScheme()
        assertSame(light, light.withOled(false))
        assertSame(dark, dark.withOled(false))
    }
}
