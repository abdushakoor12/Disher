package com.example.disher.category.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.disher.category.model.Category
import com.example.disher.category.usecase.IGetCategoriesUseCase
import com.example.disher.dishes.DishesScreen
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.ScopedServices
import kotlinx.coroutines.*

class CategoryViewModel constructor(
    val useCase: IGetCategoriesUseCase,
    val backstack: Backstack,
): ScopedServices.Registered {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _listOfCategories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val listOfCategories: State<List<Category>> = _listOfCategories

    override fun onServiceRegistered() {
        coroutineScope.launch {
            val categoriesList = useCase()
            _listOfCategories.value = categoriesList.categories
        }
    }

    override fun onServiceUnregistered() {
        coroutineScope.cancel()
    }

    fun onItemClick(category: String) {
        backstack.goTo(DishesScreen(category))
    }
}