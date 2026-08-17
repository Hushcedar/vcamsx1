package top.niunaijun.blackboxa.view.vcam

data class VCamConfig(
    val method: Int = 1,
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
