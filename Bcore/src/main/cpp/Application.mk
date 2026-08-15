# Only build for ABIs that have real Dobby prebuilt static libs.
# x86 and x86_64 are intentionally excluded — they have no DobbyHook implementation
# and will fail to link with "undefined symbol: DobbyHook".
APP_ABI := arm64-v8a armeabi-v7a

APP_PLATFORM := android-24
APP_STL      := c++_shared
APP_CPPFLAGS := -std=c++17

# Disable exceptions and RTTI for xdl (it uses -fno-exceptions internally)
APP_CFLAGS   := -fstack-protector-strong
