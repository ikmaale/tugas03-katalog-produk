package com.example.katalogproduk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.katalogproduk.ui.theme.KatalogProdukTheme
import java.util.Locale

// ======================= MODEL DATA =======================
// 6 properti; "description" nullable (String?)
data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Int,
    val stock: Int,
    val description: String?
)

// ======================= LOGIKA =======================

// Format angka menjadi Rupiah, contoh: 8500000 -> Rp 8.500.000
fun formatRupiah(value: Int): String =
    "Rp " + String.format(Locale.US, "%,d", value).replace(",", ".")

// VALIDASI INPUT: mengembalikan pesan error, atau null jika semua valid
fun validateProduct(
    name: String,
    category: String,
    priceText: String,
    stockText: String
): String? {
    if (name.isBlank()) return "Nama produk tidak boleh kosong"
    if (category.isBlank()) return "Kategori tidak boleh kosong"
    val price = priceText.toIntOrNull() ?: return "Harga harus berupa angka"
    if (price <= 0) return "Harga harus lebih dari 0"
    val stock = stockText.toIntOrNull() ?: return "Stok harus berupa angka"
    if (stock < 0) return "Stok tidak boleh negatif"
    return null
}

// FILTER: berdasarkan kategori (null = semua kategori)
fun filterProducts(products: List<Product>, category: String?): List<Product> =
    products.filter { category == null || it.category == category }

// SORT: berdasarkan harga
fun sortProducts(products: List<Product>, ascending: Boolean): List<Product> =
    if (ascending) products.sortedBy { it.price }
    else products.sortedByDescending { it.price }

// TRANSFORM/MAP: Product -> teks yang mudah dibaca
fun toDisplayText(p: Product): String =
    "${p.name} | ${p.category} | ${formatRupiah(p.price)} | Stok: ${p.stock} | " +
            (p.description ?: "Tidak ada deskripsi") // null safety dengan elvis operator

fun sampleProducts(): List<Product> = listOf(
    Product(1, "Laptop ASUS", "Elektronik", 8500000, 5, "Laptop untuk kuliah"),
    Product(2, "Mouse Wireless", "Aksesoris", 150000, 20, "Mouse tanpa kabel"),
    Product(3, "Keyboard Mechanical", "Aksesoris", 450000, 10, "Keyboard untuk mengetik"),
    Product(4, "Headset Gaming", "Aksesoris", 350000, 8, "Headset dengan microphone"),
    Product(5, "Monitor 24 Inch", "Elektronik", 1800000, 7, "Monitor Full HD"),
    Product(6, "Webcam HD", "Elektronik", 400000, 12, "Kamera untuk video call"),
    Product(7, "Flashdisk 64GB", "Penyimpanan", 90000, 30, "Flashdisk USB"),
    Product(8, "SSD 512GB", "Penyimpanan", 750000, 15, null)
)

// ======================= UI =======================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KatalogProdukTheme {
                // Scaffold + innerPadding supaya konten tidak tertabrak status bar/kamera
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CatalogScreen(
                        initialProducts = sampleProducts(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryButton(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(onClick = onClick) { Text(label) }
    } else {
        OutlinedButton(onClick = onClick) { Text(label) }
    }
}

@Composable
fun CatalogScreen(initialProducts: List<Product>, modifier: Modifier = Modifier) {
    var products by remember { mutableStateOf(initialProducts) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var ascending by remember { mutableStateOf(true) }

    // state form input
    var nameInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var stockInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var messageIsError by remember { mutableStateOf(false) }

    val categories = products.map { it.category }.distinct()
    val visibleLines = sortProducts(filterProducts(products, selectedCategory), ascending)
        .map { toDisplayText(it) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Katalog Produk Toko Elektronik", style = MaterialTheme.typography.headlineSmall)
        }

        // Tombol FILTER kategori
        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryButton("Semua", selectedCategory == null) { selectedCategory = null }
                categories.forEach { c ->
                    CategoryButton(c, selectedCategory == c) { selectedCategory = c }
                }
            }
        }

        // Tombol SORT harga
        item {
            OutlinedButton(onClick = { ascending = !ascending }) {
                Text(if (ascending) "Urutan: Harga termurah" else "Urutan: Harga termahal")
            }
        }

        item {
            Text(
                "Menampilkan ${visibleLines.size} dari ${products.size} produk",
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Daftar produk (hasil filter + sort + map)
        items(visibleLines) { line ->
            Text(line, style = MaterialTheme.typography.bodyMedium)
        }

        // Form tambah produk dengan VALIDASI INPUT
        item {
            Spacer(Modifier.height(16.dp))
            Text("Tambah Produk", style = MaterialTheme.typography.titleMedium)
        }
        item {
            OutlinedTextField(
                value = nameInput, onValueChange = { nameInput = it },
                label = { Text("Nama produk") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = categoryInput, onValueChange = { categoryInput = it },
                label = { Text("Kategori") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = priceInput, onValueChange = { priceInput = it },
                label = { Text("Harga (angka)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = stockInput, onValueChange = { stockInput = it },
                label = { Text("Stok (angka)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = descInput, onValueChange = { descInput = it },
                label = { Text("Deskripsi (boleh kosong)") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(onClick = {
                val error = validateProduct(nameInput, categoryInput, priceInput, stockInput)
                if (error != null) {
                    message = error
                    messageIsError = true
                } else {
                    val newProduct = Product(
                        id = (products.maxOfOrNull { it.id } ?: 0) + 1,
                        name = nameInput.trim(),
                        category = categoryInput.trim(),
                        price = priceInput.toInt(),
                        stock = stockInput.toInt(),
                        description = descInput.takeIf { it.isNotBlank() }?.trim() // kosong -> null
                    )
                    products = products + newProduct
                    message = "Produk '${newProduct.name}' berhasil ditambahkan"
                    messageIsError = false
                    nameInput = ""; categoryInput = ""; priceInput = ""
                    stockInput = ""; descInput = ""
                }
            }) {
                Text("Tambah")
            }
        }
        item {
            if (message.isNotEmpty()) {
                Text(
                    message,
                    color = if (messageIsError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    KatalogProdukTheme {
        CatalogScreen(initialProducts = sampleProducts())
    }
}