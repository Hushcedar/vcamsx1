package com.axiom.vcam.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CardColor   = Color(0xFF252840)
private val AccentColor = Color(0xFF6C7AE0)
private val TextColor   = Color(0xFFE0E4FF)
private val SubColor    = Color(0xFF8890AA)

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, color = SubColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            content()
        }
    }
}

@Composable
fun VCamButton(
    text:    String,
    enabled: Boolean      = true,
    icon:    ImageVector? = null,
    onClick: () -> Unit
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape    = RoundedCornerShape(10.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor         = AccentColor,
            disabledContainerColor = Color(0xFF3A3D5C)
        )
    ) {
        icon?.let { Icon(it, null, tint = Color.White, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)) }
        Text(text, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun VCamToggleRow(
    label:   String,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment      = Alignment.CenterVertically,
        horizontalArrangement  = Arrangement.SpaceBetween
    ) {
        Text(label, color = if (enabled) TextColor else SubColor, fontSize = 15.sp)
        Switch(
            checked         = checked,
            onCheckedChange = onChange,
            enabled         = enabled,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = Color.White,
                checkedTrackColor   = AccentColor,
                uncheckedThumbColor = Color(0xFF888AAA),
                uncheckedTrackColor = Color(0xFF3A3D5C),
                uncheckedBorderColor= Color(0xFF3A3D5C)
            )
        )
    }
}
