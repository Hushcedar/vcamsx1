#include <jni.h>
#include <string>
#include <dlfcn.h>
#include <unistd.h>
#include <sys/stat.h>
#include <cstring>
#include <android/log.h>
#include "dobby.h"
#include "xdl.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "AntiDetection", __VA_ARGS__)

// File hook functions
typedef int (*open_func)(const char* pathname, int flags, ...);
typedef int (*fopen_func)(const char* pathname, const char* mode);
typedef int (*stat_func)(const char* pathname, struct stat* statbuf);

static open_func orig_open = nullptr;
static fopen_func orig_fopen = nullptr;
static stat_func orig_stat = nullptr;

int my_open(const char* pathname, int flags, ...) {
    std::string path(pathname);
    if (path.find("/proc/") != std::string::npos && path.find("/maps") != std::string::npos) {
    }
    return orig_open(pathname, flags);
}

int my_fopen(const char* pathname, const char* mode) {
    return orig_fopen(pathname, mode);
}

int my_stat(const char* pathname, struct stat* statbuf) {
    return orig_stat(pathname, statbuf);
}

static void install_file_hooks() {
#if defined(__aarch64__) || defined(__arm__)
    void* handle = xdl_open("libc.so", XDL_DEFAULT);
    if (!handle) {
        LOGD("xdl_open failed for libc.so");
        return;
    }

    void* open_target = xdl_dsym(handle, "open", nullptr);
    if (open_target) {
        if (DobbyHook(open_target, (void*)my_open, (void**)&orig_open) == 0) {
            LOGD("open hook installed");
        }
    }

    void* fopen_target = xdl_dsym(handle, "fopen", nullptr);
    if (fopen_target) {
        if (DobbyHook(fopen_target, (void*)my_fopen, (void**)&orig_fopen) == 0) {
            LOGD("fopen hook installed");
        }
    }

    void* stat_target = xdl_dsym(handle, "stat", nullptr);
    if (stat_target) {
        if (DobbyHook(stat_target, (void*)my_stat, (void**)&orig_stat) == 0) {
            LOGD("stat hook installed");
        }
    }

    xdl_close(handle);
    LOGD("File system hooks installed");
#else
    LOGD("DobbyHook not available on this ABI — file hooks skipped");
#endif
}
