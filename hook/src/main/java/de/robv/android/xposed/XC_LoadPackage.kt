package de.robv.android.xposed

object XC_LoadPackage {
    class LoadPackageParam {
        var packageName: String = ""
        var classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
    }
}
