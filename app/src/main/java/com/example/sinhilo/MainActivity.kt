package com.example.sinhilo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val FondoOscuro = Color(0xFF0D0D0D)
val Morado = Color(0xFF7B2FF7)
val MoradoClaro = Color(0xFFA855F7)
val Rojo = Color(0xFFE53935)
val Superficie = Color(0xFF1A1A2E)

data class Noticia(
    val autor: String,
    val handle: String,
    val contenido: String,
    val hora: String,
    val likes: String,
    val comentarios: String,
    val avatarColor: Color
)

val noticias = listOf(
    Noticia("Breaking News", "@breaking", "🏆 Nuevo récord mundial de atletismo en los 100m. El atleta jamaicano rompe la barrera histórica.", "hace 2 min", "12.4K", "3.2K", Color(0xFFE53935)),
    Noticia("Tech Today", "@techtoday", "🤖 OpenAI lanza su nuevo modelo GPT-5. Los expertos quedan impresionados por sus capacidades.", "hace 5 min", "45.1K", "8.7K", Color(0xFF1565C0)),
    Noticia("Sports Live", "@sportslive", "⚽ La selección nacional clasifica al Mundial con gol en el último minuto. El país celebra.", "hace 8 min", "98.3K", "21.5K", Color(0xFF2E7D32)),
    Noticia("World News", "@worldnews", "🌍 Cumbre climática alcanza acuerdo histórico. 195 países firman el nuevo tratado verde.", "hace 12 min", "33.7K", "5.1K", Color(0xFFF57F17)),
    Noticia("Entertainment", "@entertain", "🎬 La película más esperada del año rompe récords en su primer fin de semana de estreno.", "hace 15 min", "67.2K", "14.8K", Color(0xFF6A1B9A))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SinHiloScreen()
            }
        }
    }
}

@Composable
fun SinHiloScreen() {
    var noticiasCargadas by remember { mutableStateOf<List<Noticia>>(emptyList()) }
    var estado by remember { mutableStateOf("idle") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOscuro)
            .statusBarsPadding() // ✅ corrige el posicionamiento
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // TopBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Rojo, Color(0xFFB71C1C))))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "❌ SIN HILO",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Feed de Noticias",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠️ UI se congela al cargar",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Banner cargando
            AnimatedVisibility(visible = estado == "cargando") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Rojo.copy(alpha = 0.15f))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔴 App congelada... espera 3 segundos",
                        color = Rojo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Banner listo
            AnimatedVisibility(visible = estado == "listo") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1B5E20).copy(alpha = 0.3f))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✅ Cargado — pero la app estuvo congelada",
                        color = Color(0xFF69F0AE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Botón
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        estado = "cargando"
                        val inicio = System.currentTimeMillis()
                        while (System.currentTimeMillis() - inicio < 3000) { }
                        noticiasCargadas = noticias
                        estado = "listo"
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Rojo),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cargar noticias (sin hilo)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Lista
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(noticiasCargadas) { noticia ->
                    NoticiaCard(noticia)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun NoticiaCard(noticia: Noticia) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Superficie,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(noticia.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = noticia.autor.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(noticia.autor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${noticia.handle} · ${noticia.hora}", color = Color.Gray, fontSize = 12.sp)
                }
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(noticia.contenido, color = Color.White, fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MailOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(noticia.comentarios, color = Color.Gray, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Rojo, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(noticia.likes, color = Color.Gray, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Compartir", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}