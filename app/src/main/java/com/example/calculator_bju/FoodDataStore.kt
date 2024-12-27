package com.example.calculator_bju

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

// Реализация StringSerializer для DataStore
object StringSerializer : androidx.datastore.core.Serializer<String> {
    override val defaultValue: String = ""

    override suspend fun readFrom(input: InputStream): String {
        return input.bufferedReader().use { it.readText() }
    }

    override suspend fun writeTo(t: String, output: OutputStream) {
        output.bufferedWriter().use { it.write(t) }
    }
}

// Инициализация DataStore для хранения списка продуктов
private val Context.foodDataStore: DataStore<String> by dataStore(
    fileName = "food_items.json",
    serializer = StringSerializer
)

// Класс FoodDataStore для управления данными списка продуктов
class FoodDataStore(private val context: Context) {
    private val gson = Gson()

    // Получение списка продуктов из DataStore
    fun getFoodItems(): Flow<List<FoodItem>> = context.foodDataStore.data
        .map { jsonString ->
            if (jsonString.isBlank()) emptyList()
            else gson.fromJson(jsonString, object : TypeToken<List<FoodItem>>() {}.type)
        }

    // Сохранение списка продуктов в DataStore
    suspend fun saveFoodItems(foodItems: List<FoodItem>) {
        context.foodDataStore.updateData { gson.toJson(foodItems) }
    }
}
