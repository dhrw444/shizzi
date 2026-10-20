package dev.shizzi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** 语言选择：跟随系统 / 简体中文 / English。 */
enum class LanguageChoice {
    SYSTEM,
    CHINESE,
    ENGLISH,
    ;

    companion object {
        fun parse(raw: String?): LanguageChoice = entries.firstOr { it.name == raw } ?: SYSTEM
    }
}

/**
 * 运行时语言状态。
 *
 * [current] 是 Compose 快照状态：设置页切换语言后，所有读取过它的组合函数会自动重组，
 * 界面文案即时切换，无需重启应用。通知、快捷磁贴等非 Compose 代码调用 [tr] 时读取当前值。
 */
object I18n {

    /** 当前生效的语言代码："zh"（简体中文）或 "en"（英文）。 */
    var current: String by mutableStateOf(if (systemPrefersChinese()) "zh" else "en")
        private set

    fun apply(choice: LanguageChoice) {
        current = when (choice) {
            LanguageChoice.CHINESE -> "zh"
            LanguageChoice.ENGLISH -> "en"
            LanguageChoice.SYSTEM -> if (systemPrefersChinese()) "zh" else "en"
        }
    }

    private fun systemPrefersChinese(): Boolean = java.util.Locale.getDefault().language == "zh"
}

/** 双语取值：英文原文在前、中文在后，按 [I18n.current] 返回其一。 */
fun tr(english: String, chinese: String): String =
    if (I18n.current == "zh") chinese else english
