package com.wangyiheng.vcamsx.ui.theme

import androidx.compose.ui.graphics.Color

// ── Backgrounds ───────────────────────────────────────────────────────────────
val ObsidianBg       = Color(0xFF0A0E17) // deep obsidian — all screen backgrounds
val SurfaceDark      = Color(0xFF121824) // dark card fill
val AppGlassBg       = Color(0xD9121824) // 85% opacity glass card fill

// ── Accents ───────────────────────────────────────────────────────────────────
val TechCyan         = Color(0xFF00E5FF) // buttons, active borders, highlights
val TechGreen        = Color(0xFF00FF66) // online status, active switches
val NeonRed          = Color(0xFFFF2D55) // errors, revoked, banned
val NeonOrange       = Color(0xFFFF6B00) // warnings

// ── Text ──────────────────────────────────────────────────────────────────────
val TextPrimary      = Color(0xFFFFFFFF) // headers, primary content
val TextSecondary    = Color(0xFF707E94) // muted labels, subtext

// ── Borders & Inputs ──────────────────────────────────────────────────────────
val GlassBorder      = Color(0x3300E5FF) // 20% cyan — subtle card outline
val GlassBorderFocus = Color(0xFF00E5FF) // 100% cyan — focused/active outline
val InputBg          = Color(0x0AFFFFFF) // 4% white — input background
val InputBorder      = Color(0x1FFFFFFF) // 12% white — inactive input border

// ── Legacy aliases (keeps existing code compiling) ────────────────────────────
val CyberBlack       = ObsidianBg
val CyberDeep        = ObsidianBg
val CyberCard        = SurfaceDark
val CyberCardAlt     = AppGlassBg
val CyberBorder      = GlassBorder
val NeonCyan         = TechCyan
val NeonGreen        = TechGreen
val NeonPurple       = Color(0xFF7B61FF) // kept for V glyph / secondary accent
val CyberText        = TextPrimary
val CyberSubtext     = TextSecondary
val CyberDim         = Color(0xFF1E243A)

// ── Material3 compat ──────────────────────────────────────────────────────────
val Purple80         = Color(0xFFD0BCFF)
val PurpleGrey80     = Color(0xFFCCC2DC)
val Pink80           = Color(0xFFEFB8C8)
val Purple40         = Color(0xFF6650a4)
val PurpleGrey40     = Color(0xFF625b71)
val Pink40           = Color(0xFF7D5260)
