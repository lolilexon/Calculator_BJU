package com.example.calculator_bju

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.calculator_bju.ui.theme.Calculator_BJUTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var themePreferences: ThemePreferences
    private lateinit var foodDataStore: FoodDataStore
    private lateinit var dietDataStore: DietDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePreferences = ThemePreferences(applicationContext)
        foodDataStore = FoodDataStore(applicationContext)
        dietDataStore = DietDataStore(applicationContext)

        setContent {
            val isDarkTheme by themePreferences.darkThemeFlow.collectAsState(initial = false)

            Calculator_BJUTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { newTheme ->
                        lifecycleScope.launch { themePreferences.saveTheme(newTheme) }
                    },
                    foodDataStore = foodDataStore,
                    dietDataStore = dietDataStore
                )
            }
        }
    }
}
