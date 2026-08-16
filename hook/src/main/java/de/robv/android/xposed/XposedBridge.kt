package de.robv.android.xposed

object XposedBridge {
    fun log(msg: String) { android.util.Log.d("Xposed", msg) }
    fun log(t: Throwable) { android.util.Log.e("Xposed", t.message ?: "", t) }
}
