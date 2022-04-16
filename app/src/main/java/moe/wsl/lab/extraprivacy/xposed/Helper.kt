package moe.wsl.lab.extraprivacy.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class Helper : IXposedHookLoadPackage {
    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        val packageName = loadPackageParam.packageName
        if (
            !packageName.startsWith("android") &&
            !packageName.startsWith("com.android")
        ) {
            return
        }
        XposedUtils.log(LogLevel.LOG_LEVEL_DEBUG, "Starting hook $packageName...")
        XposedUtils.log(LogLevel.LOG_LEVEL_INFO, "Hooked $packageName.")
    }

}
