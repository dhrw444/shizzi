package dev.shizzi

import dev.shizzi.tr

import android.Manifest
import android.os.Build

enum class AppPermission {
    NOTIFICATIONS,
    BATTERY_EXEMPTION,
}

data class PermissionStatus(
    val permission: AppPermission,
    val isGranted: Boolean,
)

val AppPermission.title: String
    get() = when (this) {
        AppPermission.NOTIFICATIONS -> tr("Notifications", "通知")
        AppPermission.BATTERY_EXEMPTION -> tr("Background activity", "后台运行")
    }

val AppPermission.rationale: String
    get() = when (this) {
        AppPermission.NOTIFICATIONS -> tr("Required to keep your session running", "保持会话运行需要此权限")
        AppPermission.BATTERY_EXEMPTION -> tr("Required for external session management", "外部会话管理依赖此权限")
    }

val AppPermission.manifestName: String?
    get() = when (this) {
        AppPermission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
        AppPermission.BATTERY_EXEMPTION -> null
    }

val AppPermission.isApplicable: Boolean
    get() = when (this) {
        AppPermission.NOTIFICATIONS -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        AppPermission.BATTERY_EXEMPTION -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
