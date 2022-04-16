package moe.wsl.lab.extraprivacy

internal class CommonConfiguration {
    companion object {
        const val APPLICATION_TAG = "ExtraPrivacy"
        const val APPLICATION_PACKAGE_NAME = "moe.wsl.lab.extraprivacy"

        var systemContext: Any? = null
        var systemPackageManageService: Any? = null
    }
}