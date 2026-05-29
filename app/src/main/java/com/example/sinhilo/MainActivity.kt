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

// ============================================================
// COLORES GLOBALES DE LA APP
// ============================================================
val FondoOscuro = Color(0xFF0D0D0D)   // Fondo principal negro
val Morado = Color(0xFF7B2FF7)         // Morado neón
val MoradoClaro = Color(0xFFA855F7)    // Morado degradado
val Rojo = Color(0xFFE53935)           // Rojo de alerta (sin hilo)
val Superficie = Color(0xFF1A1A2E)     // Color de las cards

// ============================================================
// MODELO DE DATOS
// Representa una noticia con autor, contenido, likes, etc.
// ============================================================
data class Noticia(
    val autor: String,
    val handle: String,
    val contenido: String,
    val hora: String,
    val likes: String,
    val comentarios: String,
    val avatarColor: Color
)

// ============================================================
// LISTA DE NOTICIAS SIMULADAS
// Estos datos simulan lo que vendría de una API o base de datos
// ============================================================
val noticias = listOf(
    Noticia("Breaking News", "@breaking", "🏆 Nuevo récord mundial de atletismo en los 100m. El atleta jamaicano rompe la barrera histórica.", "hace 2 min", "12.4K", "3.2K", Color(0xFFE53935)),
    Noticia("Tech Today", "@techtoday", "🤖 OpenAI lanza su nuevo modelo GPT-5. Los expertos quedan impresionados por sus capacidades.", "hace 5 min", "45.1K", "8.7K", Color(0xFF1565C0)),
    Noticia("Sports Live", "@sportslive", "⚽ La selección nacional clasifica al Mundial con gol en el último minuto. El país celebra.", "hace 8 min", "98.3K", "21.5K", Color(0xFF2E7D32)),
    Noticia("World News", "@worldnews", "🌍 Cumbre climática alcanza acuerdo histórico. 195 países firman el nuevo tratado verde.", "hace 12 min", "33.7K", "5.1K", Color(0xFFF57F17)),
    Noticia("Entertainment", "@entertain", "🎬 La película más esperada del año rompe récords en su primer fin de semana de estreno.", "hace 15 min", "67.2K", "14.8K", Color(0xFF6A1B9A))
)

// ============================================================
// ACTIVITY PRINCIPAL
// Punto de entrada de la app → lanza SinHiloScreen()
// ============================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SinHiloScreen() // Navega directamente a la pantalla principal
            }
        }
    }
}

// ============================================================
// PANTALLA PRINCIPAL — SIN HILO
// ❌ El proceso de carga se ejecuta en el HILO PRINCIPAL
// ❌ Esto congela la UI durante 3 segundos
// ❌ El usuario no puede interactuar con nada mientras carga
// ============================================================
@Composable
fun SinHiloScreen() {
    // Estado de las noticias cargadas (inicia vacío)
    var noticiasCargadas by remember { mutableStateOf<List<Noticia>>(emptyList()) }
    // Estado de la pantalla: idle → cargando → listo
    var estado by remember { mutableStateOf("idle") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOscuro)
            .statusBarsPadding() // Respeta la barra de estado del teléfono
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --------------------------------------------------
            // TOPBAR — Encabezado con gradiente rojo
            // Indica visualmente que esta es la versión SIN HILO
            // --------------------------------------------------
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

            // --------------------------------------------------
            // BANNER DE ESTADO — Solo visible mientras carga
            // Muestra que la app está congelada
            // --------------------------------------------------
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

            // --------------------------------------------------
            // BANNER DE ÉXITO — Solo visible cuando termina
            // Avisa que cargó pero que la UI estuvo bloqueada
            // --------------------------------------------------
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

            // --------------------------------------------------
            // BOTÓN DE CARGA
            // Al presionar:
            //   1. Cambia estado a "cargando"
            //   2. Bloquea el hilo principal 3 segundos (while loop)
            //   3. Carga las noticias en la lista
            //   4. Cambia estado a "listo"
            // ❌ Durante esos 3 segundos la app NO responde
            // --------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        estado = "cargando"

                        // ❌ SIN HILO: bloquea el hilo principal durante 3 segundos
                        // El usuario NO puede tocar nada mientras esto ejecuta
                        val inicio = System.currentTimeMillis()
                        while (System.currentTimeMillis() - inicio < 3000) { }
                        // Después de 3 segundos → asigna las noticias a la lista
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

            // --------------------------------------------------
            // LISTA DE NOTICIAS
            // Se muestra cuando noticiasCargadas no está vacío
            // Cada item es un NoticiaCard estilo Twitter/Instagram
            // --------------------------------------------------
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(noticiasCargadas) { noticia ->
                    NoticiaCard(noticia) // Renderiza cada noticia como card
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// ============================================================
// COMPONENTE NoticiaCard
// Muestra una noticia estilo Twitter/Instagram con:
// - Avatar con inicial del autor
// - Nombre, handle y hora de publicación
// - Contenido de la noticia
// - Acciones: comentarios, likes y compartir
// ============================================================
@Composable
fun NoticiaCard(noticia: Noticia) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Superficie,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Avatar + info del autor
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar circular con la inicial del autor
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(noticia.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = noticia.autor.take(1), // Primera letra del nombre
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
                // Ícono de opciones (tres puntos)
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Texto principal de la noticia
            Text(noticia.contenido, color = Color.White, fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
            Spacer(modifier = Modifier.height(10.dp))

            // Fila de acciones: comentarios | likes | compartir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Comentarios
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MailOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(noticia.comentarios, color = Color.Gray, fontSize = 12.sp)
                }
                // Likes (corazón rojo)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Rojo, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(noticia.likes, color = Color.Gray, fontSize = 12.sp)
                }
                // Compartir
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Compartir", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}