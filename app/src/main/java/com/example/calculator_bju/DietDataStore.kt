package com.example.calculator_bju

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dietDataStore: DataStore<String> by dataStore(
    fileName = "diet_plan.json",
    serializer = StringSerializer
)

class DietDataStore(private val context: Context) {
    private val gson = Gson()

    fun getDietPlan(): Flow<List<DietDay>> = context.dietDataStore.data
        .map { jsonString ->
            if (jsonString.isBlank()) emptyList()
            else gson.fromJson(jsonString, object : TypeToken<List<DietDay>>() {}.type)
        }

    suspend fun saveDietPlan(dietPlan: List<DietDay>) {
        context.dietDataStore.updateData { gson.toJson(dietPlan) }
    }
}
