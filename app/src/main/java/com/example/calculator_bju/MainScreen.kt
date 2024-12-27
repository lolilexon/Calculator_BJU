@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.calculator_bju

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    foodDataStore: FoodDataStore,
    dietDataStore: DietDataStore
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("home") }
    var isAddingItem by remember { mutableStateOf(false) }

    var foodItems by remember { mutableStateOf<List<FoodItem>>(emptyList()) }
    var dietPlan by remember { mutableStateOf<List<DietDay>>(emptyList()) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            foodDataStore.getFoodItems().collect { items ->
                foodItems = items
            }
        }
        scope.launch {
            dietDataStore.getDietPlan().collect { plan ->
                dietPlan = plan
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isAddingItem) {
            when (currentScreen) {
                "foodList" -> AddFoodScreen(
                    onFoodAdded = { newFoodItem ->
                        foodItems = foodItems + newFoodItem
                        isAddingItem = false
                        scope.launch { foodDataStore.saveFoodItems(foodItems) }
                    },
                    onBack = { isAddingItem = false }
                )

                "dietList" -> AddDietScreen(
                    foodItems = foodItems,
                    onDietAdded = { dietDay ->
                        dietPlan = dietPlan + dietDay
                        isAddingItem = false
                        scope.launch { dietDataStore.saveDietPlan(dietPlan) }
                    },
                    onBack = { isAddingItem = false }
                )
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Калькулятор БЖУ", color = MaterialTheme.colorScheme.onPrimary) },
                        navigationIcon = {
                            IconButton(onClick = { isDrawerOpen = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        },
                        actions = {
                            if (currentScreen != "home") {
                                FloatingActionButton(
                                    onClick = { isAddingItem = true },
                                    modifier = Modifier.size(40.dp),
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                when (currentScreen) {
                    "foodList" -> FoodListScreen()  // Теперь выводим FoodListScreen здесь
                    "dietList" -> DietList(
                        modifier = Modifier.padding(innerPadding),
                        dietPlan = dietPlan,
                        onDietPlanChange = { updatedPlan ->
                            dietPlan = updatedPlan
                            scope.launch { dietDataStore.saveDietPlan(dietPlan) }
                        },
                        onRemove = { dietDay ->
                            // Обработчик удаления элемента
                            dietPlan = dietPlan.filterNot { it.id == dietDay.id }
                            scope.launch { dietDataStore.saveDietPlan(dietPlan) }
                        }
                    )

                    else -> HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }

            if (isDrawerOpen) {
                DrawerOverlay(onClick = { isDrawerOpen = false })
                DrawerContent(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.7f)
                )
            }

            if (!isDrawerOpen) { // Кнопки отображаются только если боковая панель закрыта
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = { currentScreen = "foodList" },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth(0.3f)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        Text("Еда")
                    }

                    Button(
                        onClick = { currentScreen = "dietList" },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .fillMaxWidth(0.3f)
                            .padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        Text("Рацион")
                    }

                    IconButton(
                        onClick = { currentScreen = "home" },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(52.dp)
                            .padding(bottom = 24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.program_icon_home),
                            contentDescription = "Дом",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier) {
    Box(modifier = modifier) {
        Text("Главный экран", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun DrawerOverlay(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onClick() }
    )
}

@Composable
fun DrawerContent(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Настройки",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Тёмная тема",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(16.dp))

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) },
                    thumbContent = {
                        val iconRes = if (isDarkTheme) R.drawable.program_icon_moon else R.drawable.program_icon_sun
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = if (isDarkTheme) "Moon" else "Sun",
                            tint = Color.Unspecified
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Transparent,
                        uncheckedThumbColor = Color.Transparent,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

