package com.example.calculator_bju

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID

data class DietDay(
    val id: String = UUID.randomUUID().toString(),
    val day: String,
    val meals: List<FoodItem>
)


@Composable
fun DietList(
    modifier: Modifier = Modifier,
    dietPlan: List<DietDay>,
    onDietPlanChange: (List<DietDay>) -> Unit,
    onRemove: (FoodItem) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(dietPlan) { day ->
            DietDayItem(
                dietDay = day,
                onDelete = { deletedDay ->
                    val updatedDietPlan = dietPlan.filterNot { it.id == deletedDay.id }
                    onDietPlanChange(updatedDietPlan)
                },
                onRemove = onRemove // передаем onRemove в DietDayItem
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DietDayItem(
    dietDay: DietDay,
    onDelete: (DietDay) -> Unit,
    onRemove: (FoodItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dietDay.day,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { onDelete(dietDay) }) {
                Icon(
                    painter = painterResource(R.drawable.program_icon_trash),
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        dietDay.meals.forEach { foodItem ->
            FoodListItem(foodItem = foodItem, onRemove = { onRemove(foodItem) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
