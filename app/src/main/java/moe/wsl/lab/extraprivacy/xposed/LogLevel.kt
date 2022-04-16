package moe.wsl.lab.extraprivacy.xposed

enum class LogLevel(val logLevel: Int) {
    LOG_LEVEL_CRITICAL(5),
    LOG_LEVEL_ERROR(6),
    LOG_LEVEL_WARNING(7),
    LOG_LEVEL_INFO(8),
    LOG_LEVEL_INFO_DETAILED(9),
    LOG_LEVEL_DEBUG(10),
    LOG_LEVEL_IGNORED(11),
}