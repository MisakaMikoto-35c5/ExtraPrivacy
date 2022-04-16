package moe.wsl.lab.extraprivacy.xposed

import de.robv.android.xposed.callbacks.XC_LoadPackage

data class HookParams(
    val loadPackageParam: XC_LoadPackage.LoadPackageParam,
    val className: String,
    val methodName: String
)
