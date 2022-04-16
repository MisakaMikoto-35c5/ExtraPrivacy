package moe.wsl.lab.extraprivacy.datastruct.hookconfig

data class HookTargetClass(
    val category: String,
    val className: String,
    val methods: ArrayList<HookTargetMethod>
)
