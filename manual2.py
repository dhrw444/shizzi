import io
p = 'app/src/main/java/dev/shizzi/SessionNotification.kt'
s = io.open(p, encoding='utf-8').read()
pairs = [
 ('if (isStopping) "Cleaning up…" else "Getting ready…"',
  'if (isStopping) tr("Cleaning up…", "正在清理…") else tr("Getting ready…", "正在准备…")'),
 ('UiStatus.ERROR -> "Session ended"', 'UiStatus.ERROR -> tr("Session ended", "会话已结束")'),
 ('UiStatus.READY -> "Session ended"', 'UiStatus.READY -> tr("Session ended", "会话已结束")'),
 ('state.isVpnBypassed -> "Connected · VPN ignored"',
  'state.isVpnBypassed -> tr("Connected · VPN ignored", "已连接 · 已忽略 VPN")'),
 ('state.isVpnBound -> "Connected · VPN"', 'state.isVpnBound -> tr("Connected · VPN", "已连接 · VPN")'),
 ('        else -> "Connected"', '        else -> tr("Connected", "已连接")'),
 ('else -> "Not sharing"', 'else -> tr("Not sharing", "未共享")'),
 ('raw.contains(EXCEPTION_MARKER) -> "Something went wrong. Check the app for details."',
  'raw.contains(EXCEPTION_MARKER) ->\n            tr("Something went wrong. Check the app for details.", "出现问题，请打开应用查看详情。")'),
 ('if (isStopping) "Turning the hotspot off…" else "Turning the hotspot on…"',
  'if (isStopping) tr("Turning the hotspot off…", "正在关闭热点…") else tr("Turning the hotspot on…", "正在开启热点…")'),
 ('0 -> "No devices connected"', '0 -> tr("No devices connected", "无设备连接")'),
 ('else -> "$clients devices"', 'else -> tr("$clients devices", "$clients 台设备")'),
 ('''        null -> "Shizuku stopped, so the session ended. The hotspot was turned off. " +
            "Start again when you are ready."''',
  '''        null -> tr(
            "Shizuku stopped, so the session ended. The hotspot was turned off. " +
                "Start again when you are ready.",
            "Shizuku 已停止，因此会话结束，热点已关闭。准备好后可以再次开始。",
        )'''),
 ('''        else -> "The session ended but the hotspot may still be on: $problem — " +
            "turn it off in Settings."''',
  '''        else -> tr(
            "The session ended but the hotspot may still be on: $problem — turn it off in Settings.",
            "会话已结束，但热点可能仍在开启：$problem —— 可在设置中关闭。",
        )'''),
 ('        "Stop",', '        tr("Stop", "停止"),'),
 ('            "Tethering session",', '            tr("Tethering session", "热点会话"),'),
 ('description = "Shown while a tethering session is running"',
  'description = tr("Shown while a tethering session is running", "热点共享会话进行中时显示")'),
]
for old, new in pairs:
    assert old in s, old[:50]
    s = s.replace(old, new, 1)
if 'import dev.shizzi.tr' not in s:
    lines = s.split('\n')
    for i, ln in enumerate(lines):
        if ln.startswith('package '):
            lines.insert(i + 1, '\nimport dev.shizzi.tr')
            break
    s = '\n'.join(lines)
io.open(p, 'w', encoding='utf-8').write(s)
print('ok')
