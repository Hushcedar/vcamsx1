package de.robv.android.xposed.callbacks

class XC_LoadPackage {
    class LoadPackageParam {
        var packageName: String = ""
        var classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
    }
}
