package de.robv.android.xposed

abstract class XC_MethodHook {
    class MethodHookParam {
        var args: Array<Any?> = emptyArray()
        var result: Any? = null
        var thisObject: Any? = null
    }
    open fun beforeHookedMethod(param: MethodHookParam) {}
    open fun afterHookedMethod(param: MethodHookParam) {}
}
