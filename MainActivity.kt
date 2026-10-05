package com.azulgur.miirtree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

data class TreeNode(val id: Int, val title: String, val type: String, val x: Float, val y: Float)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { MiirTreeApp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiirTreeApp() {
    var nextId by remember { mutableIntStateOf(3) }
    var nodes by remember {
        mutableStateOf(listOf(
            TreeNode(1, "Начало", "Сцена", 80f, 150f),
            TreeNode(2, "Карта", "Изображение", 520f, 330f)
        ))
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("MiirTree · v0.1") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Добавить узел") },
                onClick = {
                    nodes = nodes + TreeNode(nextId, "Узел $nextId", "Сцена", 180f, 520f)
                    nextId++
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).background(Color(0xFFF3F3F3))) {
            Canvas(Modifier.fillMaxSize()) {
                if (nodes.size >= 2) {
                    for (i in 0 until nodes.lastIndex) {
                        val a = nodes[i]; val b = nodes[i + 1]
                        drawLine(Color.Gray, Offset(a.x + 110f, a.y + 55f), Offset(b.x + 110f, b.y + 55f), 5f)
                    }
                }
            }
            nodes.forEach { node ->
                Card(
                    modifier = Modifier
                        .offset { IntOffset(node.x.roundToInt(), node.y.roundToInt()) }
                        .width(170.dp)
                        .pointerInput(node.id) {
                            detectDragGestures { change, drag ->
                                change.consume()
                                nodes = nodes.map {
                                    if (it.id == node.id) it.copy(x = it.x + drag.x, y = it.y + drag.y) else it
                                }
                            }
                        },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(node.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(node.type, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
