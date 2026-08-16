package com.axiom.vcam.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.axiom.vcam.container.Container
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val BgColor     = Color(0xFF1A1C2E)
private val CardColor   = Color(0xFF252840)
private val AccentColor = Color(0xFF6C7AE0)

data class AppItem(
    val label:       String,
    val packageName: String,
    val icon:        Drawable?,
    val isCloned:    Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    var apps    by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Load installed apps + cloned status
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val pm           = context.packageManager
            val installed    = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val clonedPkgs   = Container.installedPackages().toSet()
            apps = installed
                .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
                .filter { it.packageName != context.packageName }
                .map {
                    AppItem(
                        label       = it.loadLabel(pm).toString(),
                        packageName = it.packageName,
                        icon        = runCatching { it.loadIcon(pm) }.getOrNull(),
                        isCloned    = it.packageName in clonedPkgs
                    )
                }
                .sortedBy { it.label }
            loading = false
        }
    }

    Scaffold(
        containerColor = BgColor,
        topBar = {
            TopAppBar(
                title          = { Text("Clone App", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors         = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1C2E))
            )
        }
    ) { pad ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentColor)
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(pad).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(apps, key = { it.packageName }) { app ->
                    AppRow(
                        app = app,
                        onClone = {
                            scope.launch {
                                val ok = Container.install(app.packageName)
                                // Refresh
                                val clonedPkgs = Container.installedPackages().toSet()
                                apps = apps.map { it.copy(isCloned = it.packageName in clonedPkgs) }
                            }
                        },
                        onLaunch   = { Container.launch(app.packageName) },
                        onUnclone  = {
                            scope.launch {
                                Container.uninstall(app.packageName)
                                val clonedPkgs = Container.installedPackages().toSet()
                                apps = apps.map { it.copy(isCloned = it.packageName in clonedPkgs) }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppRow(
    app:      AppItem,
    onClone:  () -> Unit,
    onLaunch: () -> Unit,
    onUnclone: () -> Unit
) {
    Card(
        shape  = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App icon
            app.icon?.let {
                Image(
                    bitmap             = it.toBitmap(48, 48).asImageBitmap(),
                    contentDescription = app.label,
                    modifier           = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                )
            } ?: Box(Modifier.size(40.dp).background(Color(0xFF3A3D5C), RoundedCornerShape(8.dp)))

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(app.label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(app.packageName, color = Color(0xFF8890CC), fontSize = 11.sp)
            }

            if (app.isCloned) {
                Row {
                    // Launch
                    IconButton(onClick = onLaunch) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF4CAF50))
                    }
                    // Unclone
                    IconButton(onClick = onUnclone) {
                        Icon(Icons.Default.Delete, null, tint = Color(0xFFEF5350))
                    }
                }
            } else {
                // Clone button
                TextButton(
                    onClick = onClone,
                    colors  = ButtonDefaults.textButtonColors(contentColor = AccentColor)
                ) { Text("Clone", fontWeight = FontWeight.Bold) }
            }
        }
    }
}
