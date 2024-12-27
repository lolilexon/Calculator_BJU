package com.example.calculator_bju

import FoodItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun FoodListScreen() {
    // Инициализация состояния
    val foodItems = remember { mutableStateOf<List<FoodItem>>(emptyList()) }

    // Пример начальных данных
    val initialFoodItems = listOf(
        FoodItem(
            id = UUID.randomUUID().toString(), // Генерируем уникальный ID
            name = "Яблоко",
            foodWeight = 150,
            calories = 52,
            proteins = 0.3,
            fats = 0.2,
            carbs = 14.0,
            imageRes = R.drawable.food_image_apple
        ),
        FoodItem(
            id = UUID.randomUUID().toString(), // Генерируем уникальный ID
            name = "Банан",
            foodWeight = 120,
            calories = 89,
            proteins = 1.1,
            fats = 0.3,
            carbs = 23.0,
            imageRes = R.drawable.food_image_banana
        )
    )

    // Устанавливаем начальные данные
    LaunchedEffect(Unit) {
        foodItems.value = initialFoodItems
    }

    // Функция для удаления элемента
    fun removeFoodItem(item: FoodItem) {
        foodItems.value = foodItems.value.filterNot { food -> food.id == item.id } // Удаляем по id
    }

    // Визуализация списка еды
    FoodList(
        foodItems = foodItems.value,
        onFoodItemRemoved = { item -> removeFoodItem(item) }
    )
}

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    foodItems: List<FoodItem>,
    onFoodItemRemoved: (FoodItem) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(foodItems) { item ->
            FoodListItem(foodItem = item, onRemove = { onFoodItemRemoved(item) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FoodListItem(foodItem: FoodItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = foodItem.imageRes),
            contentDescription = foodItem.name,
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) { // Используем weight для правильного распределения места
            Text(foodItem.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Text("Вес: ${foodItem.foodWeight} г", color = MaterialTheme.colorScheme.onSurface)
            Text("Калории: ${foodItem.calories}", color = MaterialTheme.colorScheme.onSurface)
            Text("Белки: ${foodItem.proteins} г", color = MaterialTheme.colorScheme.onSurface)
            Text("Жиры: ${foodItem.fats} г", color = MaterialTheme.colorScheme.onSurface)
            Text("Углеводы: ${foodItem.carbs} г", color = MaterialTheme.colorScheme.onSurface)
        }

        // Кнопка удаления
        IconButton(
            onClick = onRemove,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.program_icon_trash),
                contentDescription = "Удалить",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


