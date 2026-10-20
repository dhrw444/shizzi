package dev.shizzi.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shizzi.LanguageChoice
import dev.shizzi.tr
import dev.shizzi.ui.theme.ShizziTheme
import dev.shizzi.ui.theme.isPressed
import dev.shizzi.ui.theme.standardSpring
import dev.shizzi.ui.theme.themedIndication
import dev.shizzi.ui.theme.themedSurface

private val OptionHeight = 44.dp

private fun labelFor(choice: LanguageChoice): String = when (choice) {
    LanguageChoice.SYSTEM -> tr("Match system", "跟随系统")
    LanguageChoice.CHINESE -> "简体中文"
    LanguageChoice.ENGLISH -> "English"
}

/** 三选一语言切换：跟随系统 / 简体中文 / English。选中即刻生效。 */
@Composable
fun LanguagePicker(selected: LanguageChoice, onSelect: (LanguageChoice) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = ShizziTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(ShizziTheme.spacing.sm),
    ) {
        LanguageChoice.entries.forEach { choice ->
            LanguageOption(
                choice = choice,
                isSelected = choice == selected,
                onSelect = { onSelect(choice) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun LanguageOption(
    choice: LanguageChoice,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val colors = ShizziTheme.colors
    val isPressed = interaction.isPressed()

    val fill by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.surface,
        animationSpec = standardSpring(),
        label = "languageFill",
    )

    val tint by animateColorAsState(
        targetValue = if (isSelected) colors.onPrimary else colors.onSurfaceMuted,
        animationSpec = standardSpring(),
        label = "languageTint",
    )

    Box(
        modifier = modifier
            .height(OptionHeight)
            .themedSurface(fill = fill, isPressed = isPressed)
            .clickable(
                interactionSource = interaction,
                indication = themedIndication(),
            ) {
                onSelect()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = labelFor(choice),
            style = ShizziTheme.typography.label,
            color = tint,
        )
    }
}