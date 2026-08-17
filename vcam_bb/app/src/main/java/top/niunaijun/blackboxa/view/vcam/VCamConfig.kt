package top.niunaijun.blackboxa.view.vcam

data class VCamConfig(
    val method: Int = 1,      // 1=disable 2=local video 3=network 4=picture
    val videoUri: String = "",
    val netUrl: String = "",
    val picUri: String = "",
    val audio: Boolean = false,
    val width: Int = 1280,
    val height: Int = 720,
    val fps: Int = 30
) {
    val isActive get() = method != 1
}
