package dev.shizzi.ui.onboarding

import dev.shizzi.tr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.shizzi.CompatibilityState
import dev.shizzi.ui.theme.ShizziTheme
import dev.shizzi.ui.theme.themedSurface

@Composable
fun TetheringProviderInstallCard(state: CompatibilityState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .themedSurface(fill = ShizziTheme.colors.surface)
            .padding(ShizziTheme.spacing.lg),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(ShizziTheme.spacing.md),
    ) {
        ModuleStateIcon(state)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ShizziTheme.spacing.xs),
        ) {
            Text(
                text = titleFor(state),
                style = ShizziTheme.typography.subheading,
                color = ShizziTheme.colors.onSurface,
            )

            Text(
                text = bodyFor(state),
                style = ShizziTheme.typography.body,
                color = ShizziTheme.colors.onSurfaceMuted,
            )

            if (state is CompatibilityState.InstallFailed) {
                InstallFailureDetail(state.reason)
            }
        }
    }
}

@Composable
private fun InstallFailureDetail(reason: String) {
    Text(
        text = breakableIdentifiers(reason),
        style = ShizziTheme.typography.log,
        color = ShizziTheme.colors.onSurfaceMuted,
        modifier = Modifier.padding(top = ShizziTheme.spacing.xs),
    )
}

private fun titleFor(state: CompatibilityState): String = when (state) {
    is CompatibilityState.Staged -> tr("Restart your phone to finish", "重启手机以完成")
    is CompatibilityState.InstallFailed ->
        tr("The module wasn't accepted", "模块未被接受")
    else -> tr("Tethering module", "热点模块")
}

private fun bodyFor(state: CompatibilityState): String = when (state) {
    is CompatibilityState.Installing -> tr("Installing the module…", "正在安装模块…")

    is CompatibilityState.Staged ->
        tr(
            "The module is ready and will finish installing the next time your phone starts up. " +
                "Come back here afterwards to check compatibility.",
            "模块已就绪，将在下次手机启动时完成安装。完成后请回到此处检查兼容性。",
        )

    is CompatibilityState.InstallFailed ->
        tr(
            "Your phone refused this module, which some manufacturers' builds do. " +
                "Nothing was changed.",
            "你的手机拒绝了该模块，部分厂商的定制系统会这样。未做任何改动。",
        )

    else -> tr(
        "The module is downloaded and verified, ready to install.",
        "模块已下载并校验，可以安装。",
    )
}
