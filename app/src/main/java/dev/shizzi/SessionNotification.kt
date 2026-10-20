package dev.shizzi

import dev.shizzi.tr

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class SessionNotification(private val context: Context) {

    fun build(state: SessionUiState, isStopping: Boolean): Notification {
        createChannel()

        val text = bodyFor(state, isStopping)

        return Notification.Builder(context, CHANNEL_ID)
            .setContentTitle(titleFor(state, isStopping))
            .setContentText(text)
            .setStyle(Notification.BigTextStyle().bigText(text))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(openAppIntent())
            .setOngoing(state.status != UiStatus.ERROR)
            .addAction(stopAction())
            .build()
    }

    private fun titleFor(state: SessionUiState, isStopping: Boolean): String =
        when (state.status) {
            UiStatus.CONNECTED -> connectedTitle(state)
            UiStatus.LOADING -> if (isStopping) tr("Cleaning up…", "正在清理…") else tr("Getting ready…", "正在准备…")
            UiStatus.ERROR -> tr("Session ended", "会话已结束")
            UiStatus.READY -> tr("Session ended", "会话已结束")
        }

    private fun connectedTitle(state: SessionUiState): String = when {
        state.isVpnBypassed -> tr("Connected · VPN ignored", "已连接 · 已忽略 VPN")
        state.isVpnBound -> tr("Connected · VPN", "已连接 · VPN")
        else -> tr("Connected", "已连接")
    }

    private fun bodyFor(state: SessionUiState, isStopping: Boolean): String = when {
        state.lastError.isNotEmpty() -> userFacingError(state.lastError)
        state.status == UiStatus.LOADING -> loadingBody(isStopping)
        state.status == UiStatus.CONNECTED -> connectedBody(state)
        else -> tr("Not sharing", "未共享")
    }

    private fun userFacingError(raw: String): String = when {
        raw.contains(EXCEPTION_MARKER) ->
            tr("Something went wrong. Check the app for details.", "出现问题，请打开应用查看详情。")
        else -> raw
    }

    private fun loadingBody(isStopping: Boolean): String =
        if (isStopping) tr("Turning the hotspot off…", "正在关闭热点…") else tr("Turning the hotspot on…", "正在开启热点…")

    private fun connectedBody(state: SessionUiState): String = when (state.clientCount) {
        0 -> tr("No devices connected", "无设备连接")
        else -> "${deviceCount(state.clientCount)} · " +
            "$DOWN_ARROW ${Traffic.format(state.traffic.down)} · " +
            "$UP_ARROW ${Traffic.format(state.traffic.up)}"
    }

    private fun deviceCount(clients: Int): String = when (clients) {
        1 -> "1 device"
        else -> tr("$clients devices", "$clients 台设备")
    }

    fun describeLoss(problem: String?): String = when (problem) {
        null -> tr(
            "Shizuku stopped, so the session ended. The hotspot was turned off. " +
                "Start again when you are ready.",
            "Shizuku 已停止，因此会话结束，热点已关闭。准备好后可以再次开始。",
        )

        else -> tr(
            "The session ended but the hotspot may still be on: $problem — turn it off in Settings.",
            "会话已结束，但热点可能仍在开启：$problem —— 可在设置中关闭。",
        )
    }

    private fun openAppIntent(): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE,
    )

    private fun stopAction(): Notification.Action = Notification.Action.Builder(
        null,
        tr("Stop", "停止"),
        PendingIntent.getService(
            context,
            1,
            Intent(context, SessionService::class.java).setAction(SessionService.ACTION_STOP),
            PendingIntent.FLAG_IMMUTABLE,
        ),
    ).build()

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            tr("Tethering session", "热点会话"),
            NotificationManager.IMPORTANCE_LOW,
        ).apply { description = tr("Shown while a tethering session is running", "热点共享会话进行中时显示") }

        notificationManager().createNotificationChannel(channel)
    }

    private fun notificationManager(): NotificationManager =
        context.getSystemService(NotificationManager::class.java)

    private companion object {
        const val CHANNEL_ID = "tethering-session"

        const val EXCEPTION_MARKER = "Exception"

        const val DOWN_ARROW = "\u2193"
        const val UP_ARROW = "\u2191"
    }
}
