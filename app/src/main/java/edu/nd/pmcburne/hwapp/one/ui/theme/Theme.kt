package edu.nd.pmcburne.hwapp.one.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    // --- MAIN BRAND COLORS ---
    primary = Purple40,                 // Main buttons, active states, and TopBar background
    onPrimary = Color.White,            // Text/Icons on top of 'primary'
    primaryContainer = Color(0xFFEADDFF), // Muted version of primary (e.g., Search bar background)
    onPrimaryContainer = Color(0xFF21005D), // Text on top of 'primaryContainer'

    // --- ACCENT / SECONDARY COLORS ---
    secondary = Color(0xFF625B71),      // Less prominent components (e.g., Filter chips)
    onSecondary = Color.White,          // Text/Icons on top of 'secondary'
    secondaryContainer = Color(0xffbda5cc), // Muted secondary (Great for the 'Final' badge!)
    onSecondaryContainer = Color(0xFF1D192B),

    // --- TERTIARY / EXTRA ACCENTS ---
    tertiary = Pink40,                  // Contrast accents (e.g., input field cursors, special alerts)
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    // --- UTILITY / STATUS COLORS ---
    error = Color(0xFFB3261E),          // Used for error messages or destructive actions
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    // --- BACKGROUNDS AND SURFACES ---
    background = Color.White,           // The very bottom layer of the screen
    onBackground = Color.Black,         // Text on the main background
    surface = NcaaGrey,                 // Cards, Sheets, and Dialog backgrounds
    onSurface = Color.Black,            // Text on your GameCards

    // --- VARIANTS (Subtle differences) ---
    surfaceVariant = Color(0xFFE7E0EC), // Slightly different shade for list item backgrounds
    onSurfaceVariant = Color(0xFF49454F), // Muted text (e.g., "Status: Final" text)
    outline = Color(0xFF79747E),        // Borders and dividers
    outlineVariant = Color(0xFFCAC4D0), // Thinner or lighter dividers

    // --- SPECIAL SLOTS ---
    scrim = Color.Black,                // The color that dims the screen when a Dialog opens
    inverseSurface = Color(0xFF313033), // Used for Snackbars (dark background on light theme)
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFD0BCFF)
)

@Composable
fun NcaaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
