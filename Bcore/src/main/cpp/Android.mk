LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libdobby
LOCAL_SRC_FILES := Dobby/$(TARGET_ARCH_ABI)/libdobby.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := xdl
LOCAL_CXXFLAGS := -std=c++11 -fno-exceptions -fno-rtti
LOCAL_EXPORT_C_INCLUDES:=$(LOCAL_PATH)
LOCAL_SRC_FILES := xdl/xdl.c \
    xdl/xdl_iterate.c \
    xdl/xdl_linker.c \
    xdl/xdl_lzma.c \
    xdl/xdl_util.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := blackbox
LOCAL_SRC_FILES := BoxCore.cpp \
    hidden_api.cpp \
    IO.cpp \
    Utils/elf_util.cpp \
    Utils/VirtualSpoof.cpp \
    Utils/HexDump.cpp \
    Utils/AntiDetection.cpp \
    Hook/BaseHook.cpp \
    Hook/DexFileHook.cpp \
    Hook/FileSystemHook.cpp \
    Hook/RuntimeHook.cpp \
    Hook/UnixFileSystemHook.cpp \
    Hook/VMClassLoaderHook.cpp \
    Hook/BinderHook.cpp \
    JniHook/JniHook.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_CFLAGS += -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w -std=c++17
LOCAL_CPPFLAGS += -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w -Werror -fms-extensions
LOCAL_LDFLAGS += -Wl,--gc-sections,--strip-all,-z,max-page-size=16384
LOCAL_ARM_MODE := arm

LOCAL_CPP_FEATURES := exceptions
LOCAL_STATIC_LIBRARIES := libdobby xdl
LOCAL_LDLIBS := -llog -landroid -lz
include $(BUILD_SHARED_LIBRARY)
