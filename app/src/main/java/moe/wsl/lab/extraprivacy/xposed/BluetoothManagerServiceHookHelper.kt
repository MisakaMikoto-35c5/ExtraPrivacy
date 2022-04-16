package moe.wsl.lab.extraprivacy.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class BluetoothManagerServiceHookHelper : IXposedHookLoadPackage {
    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        val packageName = loadPackageParam.packageName
        if (
            !packageName.startsWith("android") &&
            !packageName.startsWith("com.android")
        ) {
            return
        }
        XposedUtils.debugShowMethodsInClass(loadPackageParam, "com.android.server.BluetoothManagerService")
        XposedUtils.hookMethodWithContextField(
            HookParams(
                loadPackageParam,
                "com.android.server.BluetoothManagerService",
                "getName"
            ),
            LogLevelPref(),
            "before"
        )
        XposedUtils.hookMethodWithContextField(
            HookParams(
                loadPackageParam,
                "com.android.server.BluetoothManagerService",
                "getAddress"
            ),
            LogLevelPref(),
            "before"
        )
    }
}
