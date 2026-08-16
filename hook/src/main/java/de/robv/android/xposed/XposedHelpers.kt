package de.robv.android.xposed

object XposedHelpers {
    fun findAndHookMethod(clazz: Class<*>, methodName: String, vararg args: Any?) {}
    fun findAndHookMethod(className: String, classLoader: ClassLoader, methodName: String, vararg args: Any?) {}
}
