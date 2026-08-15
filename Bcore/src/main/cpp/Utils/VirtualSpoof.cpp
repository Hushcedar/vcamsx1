#include <jni.h>
#include <string>
#include <dlfcn.h>
#include <unistd.h>
#include <sys/system_properties.h>
#include <cstring>
#include <vector>
#include <android/log.h>
#include <regex>
#include "dobby.h"
#include "xdl.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "VirtualSpoof", __VA_ARGS__)

using namespace std;

static int (*orig_system_property_get)(const char* name, char* value);

static int my_system_property_get(const char* name, char* value) {
    string propName(name);
    if (propName == "ro.product.model") {
        strcpy(value, "SM-G998B");
        return strlen(value);
    }
    if (propName == "ro.product.manufacturer") {
        strcpy(value, "samsung");
        return strlen(value);
    }
    if (propName == "ro.build.version.release") {
        strcpy(value, "13");
        return strlen(value);
    }
    if (propName == "ro.build.version.sdk") {
        strcpy(value, "33");
        return strlen(value);
    }
    if (propName == "ro.build.fingerprint") {
        strcpy(value, "samsung/beyond1lte/beyond1:13/TP1A.220624.014/G973FXXU6HVJ3:user/release-keys");
        return strlen(value);
    }
    return orig_system_property_get(name, value);
}

void install_property_get_hook() {
#if defined(__aarch64__) || defined(__arm__)
    void* handle = xdl_open("libc.so", XDL_DEFAULT);
    if (!handle) {
        LOGD("xdl_open failed for libc.so");
        return;
    }
    void* target = xdl_dsym(handle, "__system_property_get", nullptr);
    if (target) {
        if (DobbyHook(target, (void*)my_system_property_get, (void**)&orig_system_property_get) == 0) {
            LOGD("Spoof installed successfully");
        } else {
            LOGD("Spoof hook failed");
        }
    } else {
        LOGD("__system_property_get not found");
    }
    xdl_close(handle);
#else
    LOGD("DobbyHook not available on this ABI — spoof skipped");
#endif
}
