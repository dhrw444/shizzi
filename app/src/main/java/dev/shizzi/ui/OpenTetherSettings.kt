package dev.shizzi.ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import dev.shizzi.SessionLog

/**
 * Opens the stock Hotspot & tethering screen, where the hotspot name, password
 * and band live. The direct action is hidden API, so fall back to the activity
 * and then to the general network settings.
 *
 * Every candidate is resolved before it is launched. Launching first and
 * catching the failure afterwards lets a vendor ROM that answers the hidden
 * action with a blank screen win the `any` short circuit, which is why the
 * caller used to end up on nothing at all. False when none of them resolve.
 */
fun Context.openTetherSettings(): Boolean {
    val candidates = listOf(
        Intent(TETHER_SETTINGS_ACTION),
        Intent().setComponent(ComponentName(SETTINGS_PACKAGE, "$SETTINGS_PACKAGE.TetherSettings")),
        Intent().setComponent(ComponentName(SETTINGS_PACKAGE, "$SETTINGS_PACKAGE.TetherSettingsActivity")),
        Intent(Settings.ACTION_WIRELESS_SETTINGS),
    )

    val target = candidates.firstOrNull { it.resolveActivity(packageManager) != null }
    if (target == null) {
        SessionLog.warn("no settings screen for hotspot & tethering")
        return false
    }

    return try {
        startActivity(target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (absent: ActivityNotFoundException) {
        SessionLog.warn("settings screen vanished between resolve and launch: ${absent.message}")
        false
    } catch (denied: SecurityException) {
        SessionLog.warn("settings screen refused: ${denied.message}")
        false
    }
}

private const val TETHER_SETTINGS_ACTION = "android.settings.TETHER_SETTINGS"
private const val SETTINGS_PACKAGE = "com.android.settings"
