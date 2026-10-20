package dev.shizzi.ui

import dev.shizzi.tr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue

@Composable
fun ClearLogToast(
    isConfirming: Boolean,
    toasts: ToastState,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {

    val confirm by rememberUpdatedState(onConfirm)
    val cancel by rememberUpdatedState(onCancel)

    LaunchedEffect(isConfirming) {
        if (!isConfirming) {
            toasts.dismiss(ToastKeys.CLEAR_LOG)
            return@LaunchedEffect
        }

        var isAnswered = false

        toasts.show(
            Toast(
                key = ToastKeys.CLEAR_LOG,
                message = tr("Clear the log?", "清空日志？"),
                detail = tr("This action cannot be undone.", "此操作无法撤销。"),

                duration = ToastDuration.Indefinite,
                action = ToastAction(tr("Clear", "清空")) {
                    isAnswered = true
                    confirm()
                },
                onDismiss = { if (!isAnswered) cancel() },
            ),
        )
    }
}

fun clearedToast(problem: String?): Toast = when (problem) {
    null -> Toast(
        key = ToastKeys.CLEAR_LOG,
        message = tr("Log cleared", "日志已清空"),
    )

    else -> Toast(
        key = ToastKeys.CLEAR_LOG,
        message = tr("Cleared this app's entries only", "仅清除了本应用的记录"),
        detail = problem,
        duration = ToastDuration.Indefinite,
    )
}
