import os
import subprocess

base = "/tmp/xstub"
files = {
    "de/robv/android/xposed/callbacks/XC_LoadPackage.java":
        "package de.robv.android.xposed.callbacks;\n"
        "public class XC_LoadPackage {\n"
        "    public static class LoadPackageParam {\n"
        "        public String packageName;\n"
        "        public String processName;\n"
        "        public ClassLoader classLoader;\n"
        "        public boolean isFirstApplication;\n"
        "    }\n"
        "}\n",

    "de/robv/android/xposed/XC_MethodHook.java":
        "package de.robv.android.xposed;\n"
        "public abstract class XC_MethodHook {\n"
        "    public void beforeHookedMethod(MethodHookParam param) throws Throwable {}\n"
        "    public void afterHookedMethod(MethodHookParam param) throws Throwable {}\n"
        "    public class MethodHookParam {\n"
        "        public java.lang.reflect.Member method;\n"
        "        public Object thisObject;\n"
        "        public Object[] args;\n"
        "        private Object result;\n"
        "        private Throwable throwable;\n"
        "        public Object getResult() { return result; }\n"
        "        public void setResult(Object result) { this.result = result; }\n"
        "        public Throwable getThrowable() { return throwable; }\n"
        "        public boolean hasThrowable() { return throwable != null; }\n"
        "    }\n"
        "}\n",

    "de/robv/android/xposed/XposedBridge.java":
        "package de.robv.android.xposed;\n"
        "public class XposedBridge {\n"
        "    public static void log(String text) {}\n"
        "    public static void log(Throwable t) {}\n"
        "    public static Object invokeOriginalMethod(java.lang.reflect.Member method,\n"
        "        Object thisObject, Object[] args) throws Throwable { return null; }\n"
        "}\n",

    "de/robv/android/xposed/XposedHelpers.java":
        "package de.robv.android.xposed;\n"
        "public class XposedHelpers {\n"
        "    public static Object findAndHookMethod(String className, ClassLoader cl,\n"
        "        String methodName, Object... args) { return null; }\n"
        "    public static Object findAndHookMethod(Class<?> clazz,\n"
        "        String methodName, Object... args) { return null; }\n"
        "    public static Object getObjectField(Object obj, String fieldName) { return null; }\n"
        "    public static void setObjectField(Object obj, String fieldName, Object value) {}\n"
        "    public static int getIntField(Object obj, String fieldName) { return 0; }\n"
        "    public static void setIntField(Object obj, String fieldName, int value) {}\n"
        "    public static boolean getBooleanField(Object obj, String fieldName) { return false; }\n"
        "    public static void setBooleanField(Object obj, String fieldName, boolean value) {}\n"
        "    public static Object callMethod(Object obj, String methodName, Object... args) { return null; }\n"
        "    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) { return null; }\n"
        "    public static Class<?> findClass(String className, ClassLoader classLoader) { return null; }\n"
        "}\n",

    "de/robv/android/xposed/IXposedHookLoadPackage.java":
        "package de.robv.android.xposed;\n"
        "import de.robv.android.xposed.callbacks.XC_LoadPackage;\n"
        "public interface IXposedHookLoadPackage {\n"
        "    void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable;\n"
        "}\n",
}

for path, content in files.items():
    full = os.path.join(base, path)
    os.makedirs(os.path.dirname(full), exist_ok=True)
    open(full, "w").write(content)
    print(f"Wrote {full}")

srcs = [os.path.join(base, f) for f in files.keys()]
r = subprocess.run(["javac", "-source", "8", "-target", "8"] + srcs,
    capture_output=True, text=True)
print(r.stdout or ""); print(r.stderr or "")

ws = os.environ.get("GITHUB_WORKSPACE", ".")
out = os.path.join(ws, "app/libs/XposedBridgeApi.jar")
r2 = subprocess.run(["jar", "cf", out, "-C", base, "."],
    capture_output=True, text=True)
print(r2.stdout or ""); print(r2.stderr or "")
print(f"JAR size: {os.path.getsize(out)} bytes")
