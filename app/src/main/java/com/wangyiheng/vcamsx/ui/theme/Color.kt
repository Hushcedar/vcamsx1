package com.wangyiheng.vcamsx.ui.theme

import androidx.compose.ui.graphics.Color

// ── Base surfaces ─────────────────────────────────────────────────────────────
val Obsidian         = Color(0xFF080B12)  // deepest background
val Surface          = Color(0xFF0F1420)  // card background
val SurfaceRaised    = Color(0xFF141926)  // elevated card
val SurfaceStroke    = Color(0xFF1C2333)  // subtle divider/border

// ── Accents ───────────────────────────────────────────────────────────────────
val Cyan             = Color(0xFF00D9F5)  // primary action, focus rings
val CyanDim          = Color(0x2600D9F5)  // 15% cyan for button fill
val CyanSubtle       = Color(0x1100D9F5)  // 7% cyan for card tint
val Green            = Color(0xFF00E676)  // active/on states
val GreenDim         = Color(0x2200E676)  // switch track fill
val Red              = Color(0xFFFF4757)  // error / banned
val RedDim           = Color(0x22FF4757)  // error container fill
val Amber            = Color(0xFFFFB300)  // warning

// ── Text ──────────────────────────────────────────────────────────────────────
val TextHigh         = Color(0xFFEDF0F7)  // primary text
val TextMid          = Color(0xFF5A6478)  // secondary / labels
val TextLow          = Color(0xFF2E3548)  // disabled / placeholder

// ── Input / border ────────────────────────────────────────────────────────────
val StrokeDefault    = Color(0xFF1C2333)  // inactive border
val StrokeFocus      = Color(0xFF00D9F5)  // focused border
val InputFill        = Color(0x0AEDF0F7)  // input background

// ── Legacy aliases so old references compile ──────────────────────────────────
val CyberBlack       = Obsidian
val CyberDeep        = Obsidian
val CyberCard        = Surface
val CyberCardAlt     = SurfaceRaised
val CyberBorder      = SurfaceStroke
val NeonCyan         = Cyan
val NeonGreen        = Green
val NeonRed          = Red
val NeonOrange       = Amber
val NeonPurple       = Color(0xFF7C6FF7)
val TechCyan         = Cyan
val TechGreen        = Green
val ObsidianBg       = Obsidian
val SurfaceDark      = Surface
val AppGlassBg       = SurfaceRaised
val GlassBorder      = SurfaceStroke
val GlassBorderFocus = StrokeFocus
val InputBg          = InputFill
val InputBorder      = StrokeDefault
val TextPrimary      = TextHigh
val TextSecondary    = TextMid
val CyberText        = TextHigh
val CyberSubtext     = TextMid
val CyberDim         = TextLow
val ButtonColor      = Cyan

// Material3 compat
val Purple80         = Color(0xFFD0BCFF)
val PurpleGrey80     = Color(0xFFCCC2DC)
val Pink80           = Color(0xFFEFB8C8)
val Purple40         = Color(0xFF6650a4)
val PurpleGrey40     = Color(0xFF625b71)
val Pink40           = Color(0xFF7D5260)
