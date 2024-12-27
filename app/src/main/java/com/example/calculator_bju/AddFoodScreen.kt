package com.example.calculator_bju

import FoodItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.ui.text.input.KeyboardType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(onFoodAdded: (FoodItem) -> Unit, onBack: () -> Unit) {
    // Состояния для всех полей
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    // UI для добавления продукта
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок экрана
        Text("Добавить продукт", style = MaterialTheme.typography.titleLarge)

        // Поля ввода
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Название") }
        )

        // Поле для калорий
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Калории (ккал)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Поле для белков
        OutlinedTextField(
            value = proteins,
            onValueChange = { proteins = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Белки (г)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Поле для жиров
        OutlinedTextField(
            value = fats,
            onValueChange = { fats = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Жиры (г)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Поле для углеводов
        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Углеводы (г)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Поле для веса продукта
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Вес продукта (г)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        // Кнопки "Назад" и "Сохранить"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Назад")
            }

            Button(
                onClick = {
                    // Проверка на правильность введенных данных
                    if (name.isNotEmpty() && calories.isNotEmpty() && proteins.isNotEmpty()
                        && fats.isNotEmpty() && carbs.isNotEmpty() && weight.isNotEmpty()
                    ) {
                        // Преобразование строковых значений в числа
                        val foodItem = FoodItem(
                            id = UUID.randomUUID().toString(), // Генерируем уникальный ID
                            name = name,
                            imageRes = R.drawable.ic_food_placeholder, // Заглушка
                            calories = calories.toIntOrNull() ?: 0,
                            proteins = proteins.toDoubleOrNull() ?: 0.0,
                            fats = fats.toDoubleOrNull() ?: 0.0,
                            carbs = carbs.toDoubleOrNull() ?: 0.0,
                            foodWeight = weight.toIntOrNull() ?: 0 // Добавьте weight здесь
                        )

                        // Добавление нового продукта
                        onFoodAdded(foodItem)

                        // Сброс полей ввода
                        name = ""
                        calories = ""
                        proteins = ""
                        fats = ""
                        carbs = ""
                        weight = ""
                    }
                }
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddFoodScreen() {
    AddFoodScreen(onFoodAdded = {}, onBack = {})
}
