import io

def sub(path, pairs):
    s = io.open(path, encoding='utf-8').read()
    for old, new in pairs:
        assert old in s, (path, old[:40])
        s = s.replace(old, new)
    if 'import dev.shizzi.tr' not in s:
        lines = s.split('\n')
        for i, ln in enumerate(lines):
            if ln.startswith('package '):
                lines.insert(i + 1, '\nimport dev.shizzi.tr')
                break
        s = '\n'.join(lines)
    io.open(path, 'w', encoding='utf-8').write(s)
    print('ok', path)

sub('app/src/main/java/dev/shizzi/ui/onboarding/CapabilityCard.kt', [
 ('contentDescription = "Not supported",', 'contentDescription = tr("Not supported", "不支持"),'),
 ('''    Capability.TEST_NETWORK -> "Test network API"
    Capability.PREFER_TEST_NETWORKS -> "Prefer test networks"''',
  '''    Capability.TEST_NETWORK -> tr("Test network API", "测试网络 API")
    Capability.PREFER_TEST_NETWORKS -> tr("Prefer test networks", "优先使用测试网络")'''),
 ('''        "Lets the app create the test network tunnel your hotspot traffic " +
            "travels through."''',
  '''        tr(
            "Lets the app create the test network tunnel your hotspot traffic travels through.",
            "允许应用创建测试网络隧道，热点流量将通过该隧道传输。",
        )'''),
 ('''        "Lets the app route the hotspot through the test network tunnel. " +
            "Added in Android 13."''',
  '''        tr(
            "Lets the app route the hotspot through the test network tunnel. Added in Android 13.",
            "允许应用将热点流量经由测试网络隧道转发。需要 Android 13 及以上。",
        )'''),
])

sub('app/src/main/java/dev/shizzi/ui/onboarding/TetheringProviderDownloadCard.kt', [
 ('text = "Tethering module",', 'text = tr("Tethering module", "热点模块"),'),
 ('state is CompatibilityState.Downloading -> "Downloading the module…"',
  'state is CompatibilityState.Downloading -> tr("Downloading the module…", "正在下载模块…")'),
 ('''        "Couldn't reach the network to download the module. Reconnect and try again."''',
  '''        tr(
            "Couldn't reach the network to download the module. Reconnect and try again.",
            "无法连接网络下载模块，请重新连接后重试。",
        )'''),
 ('''        "The download couldn't be verified, so nothing was installed."''',
  '''        tr(
            "The download couldn't be verified, so nothing was installed.",
            "下载校验未通过，未安装任何内容。",
        )'''),
 ('''        "Your phone needs the newer tethering module to route the hotspot. " +
            "Connect to a network to download it."''',
  '''        tr(
            "Your phone needs the newer tethering module to route the hotspot. " +
                "Connect to a network to download it.",
            "你的手机需要更新版本的热点模块才能转发热点流量。请连接网络以下载该模块。",
        )'''),
 ('''        "Your phone can run this app once it has a newer tethering module. " +
            "It's about 3 MB, and installing it needs one restart."''',
  '''        tr(
            "Your phone can run this app once it has a newer tethering module. " +
                "It's about 3 MB, and installing it needs one restart.",
            "手机装上更新版本的热点模块后即可运行本应用。模块约 3 MB，安装后需要重启一次。",
        )'''),
])

sub('app/src/main/java/dev/shizzi/ui/onboarding/TetheringProviderInstallCard.kt', [
 ('is CompatibilityState.Staged -> "Restart your phone to finish"',
  'is CompatibilityState.Staged -> tr("Restart your phone to finish", "重启手机以完成")'),
 ('''is CompatibilityState.InstallFailed -> "The module wasn't accepted"''',
  '''is CompatibilityState.InstallFailed ->
        tr("The module wasn't accepted", "模块未被接受")'''),
 ('else -> "Tethering module"', 'else -> tr("Tethering module", "热点模块")'),
 ('is CompatibilityState.Installing -> "Installing the module…"',
  'is CompatibilityState.Installing -> tr("Installing the module…", "正在安装模块…")'),
 ('''        "The module is ready and will finish installing the next time your " +
            "phone starts up. Come back here afterwards to check compatibility."''',
  '''        tr(
            "The module is ready and will finish installing the next time your phone starts up. " +
                "Come back here afterwards to check compatibility.",
            "模块已就绪，将在下次手机启动时完成安装。完成后请回到此处检查兼容性。",
        )'''),
 ('''        "Your phone refused this module, which some manufacturers' builds do. " +
            "Nothing was changed."''',
  '''        tr(
            "Your phone refused this module, which some manufacturers' builds do. " +
                "Nothing was changed.",
            "你的手机拒绝了该模块，部分厂商的定制系统会这样。未做任何改动。",
        )'''),
 ('''else -> "The module is downloaded and verified, ready to install."''',
  '''else -> tr(
        "The module is downloaded and verified, ready to install.",
        "模块已下载并校验，可以安装。",
    )'''),
])
