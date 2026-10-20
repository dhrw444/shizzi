package dev.shizzi.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.shizzi.LanguageChoice
import dev.shizzi.tr
import dev.shizzi.ui.theme.AccentChoice
import dev.shizzi.ui.theme.DesignLanguage
import dev.shizzi.ui.theme.ShizziTheme
import dev.shizzi.ui.theme.ThemeChoice

data class AppearanceState(
    val theme: ThemeChoice,
    val design: DesignLanguage,
    val accent: AccentChoice,
    val customAccents: List<Int>,
    val language: LanguageChoice,
)

data class AppearanceActions(
    val onSetTheme: (ThemeChoice) -> Unit,
    val onSetDesign: (DesignLanguage) -> Unit,
    val onSetAccent: (AccentChoice) -> Unit,
    val onAddCustomAccent: (Int) -> Unit,
    val onSetLanguage: (LanguageChoice) -> Unit,
)

@Composable
fun AppearanceSection(state: AppearanceState, actions: AppearanceActions) {
    var openSheet by remember { mutableStateOf(AppearanceSheet.NONE) }

    ThemePicker(selected = state.theme, onSelect = actions.onSetTheme)

    SettingsChoice(
        label = SettingsText(title = tr("Design", "设计风格")),
        value = designLabel(state.design),
        onClick = { openSheet = AppearanceSheet.DESIGN },
    )

    SettingsChoice(
        label = SettingsText(title = tr("Accent", "强调色")),
        value = accentLabel(state.accent),
        onClick = { openSheet = AppearanceSheet.ACCENT },
    )

    Text(
        text = tr("Language", "语言"),
        style = ShizziTheme.typography.label,
        color = ShizziTheme.colors.onSurfaceMuted,
    )

    LanguagePicker(selected = state.language, onSelect = actions.onSetLanguage)

    val dismiss = { openSheet = AppearanceSheet.NONE }

    when (openSheet) {
        AppearanceSheet.NONE -> Unit

        AppearanceSheet.DESIGN -> DesignPicker(
            selected = state.design,
            onSelect = actions.onSetDesign,
            onDismiss = dismiss,
        )

        AppearanceSheet.ACCENT -> AccentPicker(
            state = AccentPickerState(
                selected = state.accent,
                customAccents = state.customAccents,
            ),
            actions = AccentPickerActions(
                onSelect = actions.onSetAccent,
                onAddCustom = actions.onAddCustomAccent,
                onDismiss = dismiss,
            ),
        )
    }
}

private enum class AppearanceSheet { NONE, DESIGN, ACCENT }
