import os, subprocess, glob

base = "/tmp/ijkstub"
files = {
    "tv/danmaku/ijk/media/player/IjkMediaPlayer.java":
        "package tv.danmaku.ijk.media.player;\n"
        "import android.view.Surface;\n"
        "public class IjkMediaPlayer {\n"
        "    public static final int OPT_CATEGORY_FORMAT = 1;\n"
        "    public static final int OPT_CATEGORY_CODEC  = 2;\n"
        "    public static final int OPT_CATEGORY_PLAYER = 4;\n"
        "    public void setOption(int category, String name, String value) {}\n"
        "    public void setOption(int category, String name, long value) {}\n"
        "    public void setDataSource(String path) throws Exception {}\n"
        "    public void setSurface(Surface surface) {}\n"
        "    public void prepareAsync() {}\n"
        "    public void start() {}\n"
        "    public void pause() {}\n"
        "    public void stop() {}\n"
        "    public void release() {}\n"
        "    public void seekTo(long msec) {}\n"
        "    public boolean isPlaying() { return false; }\n"
        "    public String getDataSource() { return null; }\n"
        "    public interface OnPreparedListener { void onPrepared(IjkMediaPlayer mp); }\n"
        "    public interface OnErrorListener { boolean onError(IjkMediaPlayer mp, int what, int extra); }\n"
        "    public interface OnCompletionListener { void onCompletion(IjkMediaPlayer mp); }\n"
        "    public void setOnPreparedListener(OnPreparedListener l) {}\n"
        "    public void setOnErrorListener(OnErrorListener l) {}\n"
        "    public void setOnCompletionListener(OnCompletionListener l) {}\n"
        "    public void setVolume(float l, float r) {}\n"
        "    public int getVideoWidth() { return 0; }\n"
        "    public int getVideoHeight() { return 0; }\n"
        "    public long getCurrentPosition() { return 0; }\n"
        "    public long getDuration() { return 0; }\n"
        "}\n",
}

for path, content in files.items():
    full = os.path.join(base, path)
    os.makedirs(os.path.dirname(full), exist_ok=True)
    open(full, "w").write(content)
    print(f"Wrote {full}")

jars = glob.glob("/usr/local/lib/android/sdk/platforms/*/android.jar")
android_jar = jars[0] if jars else ""
print(f"Using android.jar: {android_jar}")

srcs = [os.path.join(base, f) for f in files.keys()]
cmd = ["javac", "-source", "8", "-target", "8"]
if android_jar:
    cmd += ["-classpath", android_jar]
cmd += srcs

r = subprocess.run(cmd, capture_output=True, text=True)
print(r.stdout or ""); print(r.stderr or "")

ws = os.environ.get("GITHUB_WORKSPACE", ".")
out = os.path.join(ws, "app/libs/ijkplayer-java.jar")
r2 = subprocess.run(["jar", "cf", out, "-C", base, "."], capture_output=True, text=True)
print(r2.stdout or ""); print(r2.stderr or "")
print(f"IjkPlayer stub size: {os.path.getsize(out)} bytes")
