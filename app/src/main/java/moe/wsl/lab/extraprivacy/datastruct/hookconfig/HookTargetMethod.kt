package moe.wsl.lab.extraprivacy.datastruct.hookconfig

data class HookTargetMethod(
    val name: String,
    val defaultValue: String,
    val permission: String?,
    val valueType: String = "string",
    val usermod: Boolean = false,
    val comment: String? = null,
    val runOn: String = "after"
)
