package com.example.katalogproduk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.katalogproduk.ui.theme.KatalogProdukTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatalogProdukTheme {
                CatalogScreen()
            }
        }
    }
}
