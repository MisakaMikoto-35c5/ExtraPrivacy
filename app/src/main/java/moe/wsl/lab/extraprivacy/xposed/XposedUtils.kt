package moe.wsl.lab.extraprivacy.xposed

import android.app.AndroidAppHelper
import android.content.Context
import android.content.res.Resources
import android.os.Binder
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import moe.wsl.lab.extraprivacy.CommonConfiguration
import java.lang.reflect.Method

class XposedUtils {
    companion object {
        var minLogLevel = LogLevel.LOG_LEVEL_IGNORED
        //var minLogLevel = LogLevel.LOG_LEVEL_DEBUG
        private val mContext = AndroidAppHelper.currentApplication()!!

        /**
         * Hook method with common function and safe error catch.
         */
        fun hookMethodWithContextField(
            hookParams: HookParams,
            logLevelPref: LogLevelPref,
            runOn: String
        ): XC_MethodHook.Unhook? {
            val cb = when (runOn) {
                "after" -> object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val packageName = getCallingPackageNameWithContextField(mContext)
                        log(logLevelPref.userdbg, "$packageName called method ${hookParams.className}.${hookParams.methodName}")
                        if (hookParams.loadPackageParam.packageName == "android") {
                            return
                        }
                    }

                }
                else -> object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val packageName = getCallingPackageNameWithContextField(mContext)
                        log(logLevelPref.userdbg, "$packageName called method ${hookParams.className}.${hookParams.methodName}")
                        if (hookParams.loadPackageParam.packageName == "android") {
                            return
                        }
                    }
                }
            }
            try {
                val hookResult = findAndHookMethod(hookParams, cb)
                log(logLevelPref.debug, "Hook ${hookParams.className}.${hookParams.methodName} succeed.")
                return hookResult
            } catch (e: Throwable) {
                log(logLevelPref.hookFailed, "Failed to hook ${hookParams.className}.${hookParams.methodName}.", e)
            }
            return null
        }

        fun hookMethod(
            method: Method,
            callback: XC_MethodHook
        ): XC_MethodHook.Unhook? {
            return XposedBridge.hookMethod(method, callback)
        }

        /**
         * Hook method with custom function and safe error catch.
         */
        fun findAndHookMethod(
            hookParams: HookParams,
            logLevelPref: LogLevelPref,
            hookFunction: XC_MethodHook
        ): XC_MethodHook.Unhook? {
            try {
                return findAndHookMethod(hookParams, hookFunction)
            } catch (e: Throwable) {
                log(logLevelPref.hookFailed, "Failed to hook ${hookParams.className}.${hookParams.methodName}.", e)
            }
            return null
        }

        fun findAndHookMethod(hookParams: HookParams, callback: XC_MethodHook): XC_MethodHook.Unhook? {
            val method = findMethod(hookParams)
            return hookMethod(method, callback)
        }


        fun findMethod(hookParams: HookParams): Method {
            val xposedClass = XposedHelpers.findClass(
                hookParams.className,
                hookParams.loadPackageParam.classLoader,
            )
            val methods = xposedClass.methods
            var method: Method? = null
            for (i in methods) {
                if (i.name.equals(hookParams.methodName)) {
                    method = i
                    break
                }
            }
            if (method == null) {
                throw NoSuchMethodError("Can not found ${hookParams.className}.${hookParams.methodName}")
            }
            return method
        }

        @Deprecated("Debug use only.")
        fun debugShowMethodsInClass(
            loadPackageParam: XC_LoadPackage.LoadPackageParam,
            className: String
        ) {
            try {
                val xposedClass = XposedHelpers.findClass(
                    className,
                    loadPackageParam.classLoader
                )
                val methods = xposedClass.methods
                for (i in methods) {
                    log(LogLevel.LOG_LEVEL_IGNORED, "Found ${className}.${i.name}()")
                }
            } catch (e: Throwable) {
                log(LogLevel.LOG_LEVEL_DEBUG, "Failed to display methods in ${className}.", e)
            }
        }

        private fun parseLogLevel(logLevel: LogLevel): String {
            return when (logLevel) {
                LogLevel.LOG_LEVEL_CRITICAL -> "CRITICAL"
                LogLevel.LOG_LEVEL_ERROR -> "ERROR"
                LogLevel.LOG_LEVEL_WARNING -> "WARN"
                LogLevel.LOG_LEVEL_INFO -> "INFO"
                LogLevel.LOG_LEVEL_INFO_DETAILED -> "INFO_D"
                LogLevel.LOG_LEVEL_DEBUG -> "DEBUG"
                LogLevel.LOG_LEVEL_IGNORED -> "IGNORED"
                else -> "UNKNOWN"
            }
        }

        internal fun log(logLevel: LogLevel, content: String, e: Throwable? = null) {
            if (logLevel.logLevel > minLogLevel.logLevel) return

            val stackTrace = if (e == null) {
                ""
            } else {
                "\n" + e.stackTraceToString()
            }
            XposedBridge.log("[${CommonConfiguration.APPLICATION_TAG}] [${parseLogLevel(logLevel)}] $content$stackTrace")
        }

        internal fun getCallingPackageNameWithContextField(context: Context): String? {
            try {
                val uid = Binder.getCallingUid()
                val packageManagerService = context.packageManager
                return packageManagerService.getNameForUid(uid)
            } catch (e: Throwable) {
                log(LogLevel.LOG_LEVEL_CRITICAL, "Failed to get calling package name.", e)
                return null
            }
        }

        private fun loadConfiguration() {
            val res = mContext.packageManager.getResourcesForApplication(
                CommonConfiguration.APPLICATION_PACKAGE_NAME
            )
            val staticHookConfigInputStream = res.assets.open("base_hook_config.yaml")
        }
    }
}