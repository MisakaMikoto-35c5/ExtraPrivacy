package moe.wsl.lab.extraprivacy.xposed

data class LogLevelPref(
    var default: LogLevel = LogLevel.LOG_LEVEL_INFO,
    var userdbg: LogLevel = LogLevel.LOG_LEVEL_INFO_DETAILED,
    var debug: LogLevel = LogLevel.LOG_LEVEL_DEBUG,
    var develop: LogLevel = LogLevel.LOG_LEVEL_DEBUG,
    var hookFailed: LogLevel = LogLevel.LOG_LEVEL_ERROR,
)
