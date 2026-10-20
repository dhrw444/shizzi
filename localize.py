import re, os, sys, json

# 英中对照表：键为 Kotlin 源码中双引号内的原始文本（含转义形式），值为中文
D = {
"About": "关于",
"Accent": "强调色",
"Action": "动作",
"Add color": "添加颜色",
"Added in Android 13.": "需要 Android 13 及以上。",
"Advanced": "高级",
"Allows tasker apps to manage Shizzi": "允许 Tasker 类应用管理 Shizzi",
"Always": "始终",
"Appearance": "外观",
"Author": "作者",
"Auto": "自动",
"Automation": "自动化",
"Back": "返回",
"Background activity": "后台运行",
"Bind to active VPN if available": "有活动 VPN 时绑定到该 VPN",
"Broadcast receiver": "广播接收器",
"CLEAR": "清空",
"COPY $count LINES": "复制 $count 行",
"COPY 1 LINE": "复制 1 行",
"COPY ALL": "复制全部",
"Check": "检查",
"Class": "类名",
"Cleaning up\u2026": "正在清理\u2026",
"Clear": "清空",
"Clear the log?": "清空日志？",
"Cleared this app's entries only": "仅清除了本应用的记录",
"Color picker": "颜色选择器",
"Compatibility": "兼容性",
"Connect to a network to download it.": "请连接网络以完成下载。",
"Connected": "已连接",
"Connected \u00b7 VPN": "已连接 \u00b7 VPN",
"Connected \u00b7 VPN ignored": "已连接 \u00b7 已忽略 VPN",
"Connecting": "正在连接",
"Continue": "继续",
"Copy token": "复制令牌",
"Couldn't reach the network to download the module. Reconnect and try again.": "无法连接网络下载模块，请重新连接后重试。",
"DESELECT ALL": "取消全选",
"Dark": "深色",
"Default": "默认",
"Design": "设计风格",
"Developer": "开发者",
"Diagnostics completed": "诊断完成",
"Diagnostics failed": "诊断失败",
"Download": "下载",
"Download failed": "下载失败",
"Downloaded and verified": "已下载并校验",
"Downloading the module\u2026": "正在下载模块\u2026",
"Enable logging": "启用日志",
"Export": "导出",
"Extra name": "附加参数名",
"Extra value": "附加参数值",
"Failed": "失败",
"Finish": "完成",
"GRANT PERMISSION": "授予权限",
"Get started": "开始使用",
"Getting ready\u2026": "正在准备\u2026",
"Grant": "授予",
"Grant permissions": "授予权限",
"Hard edges, bold borders, offset shadows": "硬朗边角、粗描边、偏移阴影",
"Ignore active VPN": "忽略活动 VPN",
"Install": "安装",
"Install failed": "安装失败",
"Installing": "正在安装",
"Installing the module\u2026": "正在安装模块\u2026",
"It's about 3 MB, and installing it needs one restart.": "约 3 MB，安装后需要重启一次。",
"Light": "浅色",
"Log": "日志",
"Log cleared": "日志已清空",
"Logging": "日志记录",
"Logging is disabled": "日志记录已关闭",
"Logs will appear here": "日志将显示在这里",
"Match system": "跟随系统",
"Module available": "模块可用",
"More options": "更多选项",
"NO SHIZUKU": "无 SHIZUKU",
"Neobrutalism": "新粗野主义",
"Never": "从不",
"No devices": "无设备",
"No devices connected": "无设备连接",
"No logs yet": "暂无日志",
"Not connected": "未连接",
"Not installed": "未安装",
"Not running": "未运行",
"Not sharing": "未共享",
"Not supported": "不支持",
"Nothing was changed.": "未做任何改动。",
"Notifications": "通知",
"PERMISSION NEEDED": "需要权限",
"Package": "包名",
"Permission required": "需要权限",
"Permissions": "权限",
"Prefer test networks": "优先使用测试网络",
"Ready": "就绪",
"Refuse connection without active VPN": "无活动 VPN 时拒绝连接",
"Regenerate token": "重新生成令牌",
"Report a bug": "问题反馈",
"Required for core functionality": "核心功能依赖此权限",
"Required for external session management": "外部会话管理依赖此权限",
"Required to keep your session running": "保持会话运行需要此权限",
"Restart": "重启",
"Restart onboarding": "重新引导",
"Restart required": "需要重启",
"Restart your phone to finish": "重启手机以完成",
"Retry": "重试",
"Rootless wifi-tethering with Shizuku.": "借助 Shizuku 实现免 root 的 Wi-Fi 热点共享。",
"Rounded surfaces, tonal color, soft motion": "圆润表面、色调统一、柔和动效",
"Run diagnostics": "运行诊断",
"Running diagnostics...": "正在运行诊断\u2026",
"SCROLL TO BOTTOM": "滚动到底部",
"SCROLL TO TOP": "滚动到顶部",
"SELECT ALL": "全选",
"SHIZUKU OFF": "SHIZUKU 已关闭",
"STATUS ": "状态 ",
"Send a broadcast intent with these values.": "用下列参数发送广播。",
"Service": "服务",
"Session ended": "会话已结束",
"Settings": "设置",
"Setup": "配置",
"Shizuku is installed but not running": "已安装 Shizuku，但未运行",
"Shizuku is not installed": "未安装 Shizuku",
"Shizuku not installed": "未安装 Shizuku",
"Shizuku not running": "Shizuku 未运行",
"Shizzi needs permission from Shizuku": "Shizzi 需要 Shizuku 的授权",
"Shown while a tethering session is running": "热点共享会话进行中时显示",
"Something went wrong. Check the app for details.": "出现问题，请打开应用查看详情。",
"Start": "开始",
"Start a session": "开始会话",
"Start again when you are ready.": "准备好后可以再次开始。",
"Starting": "正在启动",
"Starting\u2026": "正在启动\u2026",
"Status": "状态",
"Stop": "停止",
"Stopping\u2026": "正在停止\u2026",
"Supported": "支持",
"TUNNEL ACTIVE": "隧道已启用",
"Tap to retry": "点按重试",
"Tap to share": "点按共享",
"Target": "目标",
"Test network API": "测试网络 API",
"Tethering module": "热点模块",
"Tethering session": "热点会话",
"The download couldn't be verified, so nothing was installed.": "下载校验未通过，未安装任何内容。",
"The module is downloaded and verified, ready to install.": "模块已下载并校验，可以安装。",
"The module wasn't accepted": "模块未被接受",
"This action cannot be undone.": "此操作无法撤销。",
"This device is compatible": "此设备兼容",
"This device is not compatible": "此设备不兼容",
"Toggle": "开关",
"Token": "令牌",
"Turning the hotspot off\u2026": "正在关闭热点\u2026",
"Turning the hotspot on\u2026": "正在开启热点\u2026",
"VPN CONNECTED": "VPN 已连接",
"VPN IGNORED": "已忽略 VPN",
"VPN disconnected. Session stopped to keep devices protected.": "VPN 已断开，为保护设备已停止会话。",
"Version": "版本",
"View logs": "查看日志",
"View setup instructions": "查看配置说明",
"Wallpaper": "壁纸",
"Welcome": "欢迎",
"turn it off in Settings.": "可在设置中关闭。",
"1 device": "1 台设备",
"$clients devices": "$clients 台设备",
"1 device \u00b7 ${Traffic.format(session.traffic.down)}": "1 台设备 \u00b7 ${Traffic.format(session.traffic.down)}",
"${session.clientCount} devices \u00b7 ${Traffic.format(session.traffic.down)}": "${session.clientCount} 台设备 \u00b7 ${Traffic.format(session.traffic.down)}",
"Copy ${field.label.lowercase()}": "复制${field.label.lowercase()}",
}

# 多行字符串拼接 / 日志 / 内部键值：不做批量替换，单独手工处理
MANUAL = {"CapabilityCard.kt", "TetheringProviderDownloadCard.kt",
          "TetheringProviderInstallCard.kt", "SessionNotification.kt"}

LIT = re.compile(r'"((?:[^"\\]|\\.)*)"')
root = 'app/src/main/java'
changed = {}
for dirpath, _, files in os.walk(root):
    for fn in files:
        if not fn.endswith('.kt') or fn in MANUAL:
            continue
        p = os.path.join(dirpath, fn)
        src = open(p, encoding='utf-8').read()
        if 'tr(' in src and 'dev.shizzi.tr' in src:
            pass
        out, hits = [], 0
        i = 0
        while True:
            m = LIT.search(src, i)
            if not m:
                out.append(src[i:])
                break
            raw = m.group(1)
            if raw in D:
                # 避免重复包裹：检查前面是否已经是 tr(
                before = src[max(0, m.start()-4):m.start()]
                if before.endswith('tr('):
                    out.append(src[i:m.end()])
                else:
                    escaped_zh = D[raw]
                    out.append(src[i:m.start()])
                    out.append('tr("%s", "%s")' % (raw, escaped_zh))
                    hits += 1
            else:
                out.append(src[i:m.end()])
            i = m.end()
        new = ''.join(out)
        if hits:
            if 'import dev.shizzi.tr' not in new:
                # 插到 package 行之后，保持文件头整洁
                lines = new.split('\n')
                for idx, ln in enumerate(lines):
                    if ln.startswith('package '):
                        lines.insert(idx + 1, '\nimport dev.shizzi.tr')
                        break
                new = '\n'.join(lines)
            open(p, 'w', encoding='utf-8').write(new)
            changed[fn] = hits

print("改动文件数:", len(changed), "替换总数:", sum(changed.values()))
for k, v in sorted(changed.items(), key=lambda x: -x[1]):
    print(f"  {v:3d}  {k}")
