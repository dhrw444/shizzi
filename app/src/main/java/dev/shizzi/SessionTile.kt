package dev.shizzi

import dev.shizzi.tr

import android.service.quicksettings.Tile

enum class TileAction { START, STOP, OPEN_APP, NONE }

data class TileRender(
    val state: Int,
    val subtitle: String,
    val action: TileAction,
)

object SessionTile {

    const val LABEL = "Shizzi"

    fun render(session: SessionUiState, shizuku: ShizukuState, isStopping: Boolean): TileRender {
        if (shizuku !is ShizukuState.Ready) return unavailable(shizuku)

        return when (session.status) {
            UiStatus.LOADING -> TileRender(
                state = Tile.STATE_UNAVAILABLE,
                subtitle = if (isStopping) tr("Stopping…", "正在停止…") else tr("Starting…", "正在启动…"),
                action = TileAction.NONE,
            )

            UiStatus.CONNECTED -> TileRender(
                state = Tile.STATE_ACTIVE,
                subtitle = describeConnected(session),
                action = TileAction.STOP,
            )

            UiStatus.ERROR -> TileRender(
                state = Tile.STATE_INACTIVE,
                subtitle = tr("Tap to retry", "点按重试"),
                action = TileAction.START,
            )

            UiStatus.READY -> TileRender(
                state = Tile.STATE_INACTIVE,
                subtitle = tr("Tap to share", "点按共享"),
                action = TileAction.START,
            )
        }
    }

    private fun unavailable(shizuku: ShizukuState) = TileRender(
        state = Tile.STATE_UNAVAILABLE,
        subtitle = describe(shizuku),
        action = TileAction.OPEN_APP,
    )

    private fun describe(shizuku: ShizukuState): String = when (shizuku) {
        ShizukuState.NotInstalled -> tr("Shizuku not installed", "未安装 Shizuku")
        ShizukuState.NotRunning -> tr("Shizuku not running", "Shizuku 未运行")
        ShizukuState.PermissionRequired -> tr("Permission required", "需要权限")
        is ShizukuState.Ready -> ""
    }

    private fun describeConnected(session: SessionUiState): String = when (session.clientCount) {
        0 -> tr("No devices", "无设备")
        1 -> tr("1 device · ${Traffic.format(session.traffic.down)}", "1 台设备 · ${Traffic.format(session.traffic.down)}")
        else -> tr("${session.clientCount} devices · ${Traffic.format(session.traffic.down)}", "${session.clientCount} 台设备 · ${Traffic.format(session.traffic.down)}")
    }
}
