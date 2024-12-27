package com.example.calculator_bju

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDietScreen(
    foodItems: List<FoodItem>,  // Параметр для передачи списка еды
    onDietAdded:(DietDay) -> Unit,
    onBack: () -> Unit
) {
    // Состояния для выбранных блюд и дня недели
    var selectedDay by remember { mutableStateOf("Понедельник") }
    var selectedMeals by remember { mutableStateOf<List<FoodItem>>(emptyList()) }
    var expandedDayMenu by remember { mutableStateOf(false) }
    var expandedFoodMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Выбор дня недели
        Text("Добавить день рациона", style = MaterialTheme.typography.titleLarge)

        ExposedDropdownMenuBox(
            expanded = expandedDayMenu,
            onExpandedChange = { expandedDayMenu = !expandedDayMenu },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedDay,
                onValueChange = {},
                label = { Text("Выберите день недели") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "DropDown")
                }
            )
            ExposedDropdownMenu(
                expanded = expandedDayMenu,
                onDismissRequest = { expandedDayMenu = false }
            ) {
                listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье").forEach { day ->
                    DropdownMenuItem(
                        text = { Text(day) },
                        onClick = {
                            selectedDay = day
                            expandedDayMenu = false
                        }
                    )
                }
            }
        }

        // Выбор блюд
        Text("Выберите блюда:", style = MaterialTheme.typography.bodyLarge)

        ExposedDropdownMenuBox(
            expanded = expandedFoodMenu,
            onExpandedChange = { expandedFoodMenu = !expandedFoodMenu },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedMeals.joinToString(", ") { it.name },
                onValueChange = {},
                label = { Text("Выберите блюда") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "DropDown")
                }
            )
            ExposedDropdownMenu(
                expanded = expandedFoodMenu,
                onDismissRequest = { expandedFoodMenu = false }
            ) {
                foodItems.forEach { foodItem ->
                    DropdownMenuItem(
                        text = { Text(foodItem.name) },
                        onClick = {
                            if (!selectedMeals.contains(foodItem)) {
                                selectedMeals = selectedMeals + foodItem
                            }
                            expandedFoodMenu = false
                        }
                    )
                }
            }
        }

        // Отображение выбранных блюд
        if (selectedMeals.isNotEmpty()) {
            Text("Выбранные блюда:", style = MaterialTheme.typography.bodyMedium)
            selectedMeals.forEach { foodItem ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Image(
                        painter = painterResource(id = foodItem.imageRes),
                        contentDescription = foodItem.name,
                        modifier = Modifier.size(48.dp).padding(end = 8.dp)
                    )
                    Column {
                        Text(foodItem.name, style = MaterialTheme.typography.bodyLarge)
                        Text("Калории: ${foodItem.calories}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Кнопки для сохранения или возврата
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Назад")
            }

            Button(
                onClick = {
                    if (selectedDay.isNotEmpty() && selectedMeals.isNotEmpty()) {
                        onDietAdded(DietDay(day = selectedDay, meals = selectedMeals))
                    }
                },
                enabled = selectedDay.isNotEmpty() && selectedMeals.isNotEmpty()
            ) {
                Text("Сохранить")
            }
        }
    }
}



