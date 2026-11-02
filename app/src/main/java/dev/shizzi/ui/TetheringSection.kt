package dev.shizzi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import dev.shizzi.UsbTethering
import dev.shizzi.tr

@Composable
fun TetheringSection(
    selected: UsbTethering,
    onSelect: (UsbTethering) -> Unit,
    toasts: ToastState,
) {
    val context = LocalContext.current
    var isPickerOpen by remember { mutableStateOf(false) }

    SettingsChoice(
        label = SettingsText(title = tr("USB tethering", "USB 共享")),
        value = usbTetheringLabel(selected),
        onClick = { isPickerOpen = true },
    )

    SettingsAction(
        label = SettingsText(title = tr("Open system settings", "打开系统设置")),
        isExternal = true,
        onClick = {
            if (!context.openTetherSettings()) {
                toasts.show(unopenedToast(tr("system settings", "系统设置")))
            }
        },
    )

    if (!isPickerOpen) return

    UsbTetheringPicker(
        selected = selected,
        onSelect = onSelect,
        onDismiss = { isPickerOpen = false },
    )
}
