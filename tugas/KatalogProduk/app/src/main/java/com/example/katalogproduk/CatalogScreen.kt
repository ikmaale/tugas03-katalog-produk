package com.example.katalogproduk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.katalogproduk.ui.theme.KatalogProdukTheme

// Data sederhana untuk satu produk (static, belum ada state/interaksi kompleks)
data class Product(
    val name: String,
    val category: String,
    val price: String,
    val rating: String,
    val emoji: String
)

private val sampleProducts = listOf(
    Product("Earbuds Nirkabel", "Audio", "Rp 349.000", "4,8", "🎧"),
    Product("Smartwatch Fit", "Wearable", "Rp 799.000", "4,6", "⌚"),
    Product("Powerbank 10.000 mAh", "Aksesori", "Rp 189.000", "4,7", "🔋"),
    Product("Keyboard Mekanik", "Komputer", "Rp 559.000", "4,5", "⌨️")
)

// ---------- SCREEN ----------
// Struktur: CatalogHeader -> ProductCard (berulang) -> InfoFooter
@Composable
fun CatalogScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CatalogHeader(
                    title = "Toko Gadget",
                    subtitle = "Pilihan produk terbaik untuk kebutuhan harianmu"
                )
            }
            items(sampleProducts) { product ->
                ProductCard(product = product)
            }
            item {
                InfoFooter(text = "Harga belum termasuk ongkos kirim")
            }
        }
    }
}

// ---------- CUSTOM COMPOSABLE 1: Header ----------
@Composable
fun CatalogHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

// ---------- CUSTOM COMPOSABLE 2: Kartu produk (reusable) ----------
@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProductImage(emoji = product.emoji)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.rating,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = { /* aksi belum diwajibkan pada minggu ini */ }) {
                    Text("🛒 Beli")
                }
            }
        }
    }
}

// ---------- CUSTOM COMPOSABLE 3: Gambar produk (Box) ----------
@Composable
fun ProductImage(
    emoji: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 32.sp)
    }
}

// ---------- CUSTOM COMPOSABLE 4: Info footer ----------
@Composable
fun InfoFooter(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ---------- PREVIEW ----------
@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    KatalogProdukTheme {
        CatalogScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    KatalogProdukTheme {
        ProductCard(
            product = Product("Earbuds Nirkabel", "Audio", "Rp 349.000", "4,8", "🎧"),
            modifier = Modifier.padding(16.dp)
        )
    }
}
