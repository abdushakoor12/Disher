package com.example.disher.dishes.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disher.detail.DetailScreen
import com.example.disher.dishes.model.DishesResponse
import com.example.disher.dishes.model.Meal
import com.example.disher.dishes.usecase.IGetDishesUseCase
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.ScopedServices
import kotlinx.coroutines.*

sealed class ViewState {
    object Loading : ViewState()
    data class Success(val data: DishesResponse) : ViewState()
    data class Error(val errorMessage: String) : ViewState()
}

class DishesViewModel constructor(
    val usecase: IGetDishesUseCase,
    val category: String,
    val backstack: Backstack,
) : ScopedServices.Registered {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _viewState: MutableState<ViewState> = mutableStateOf(ViewState.Loading)
    val viewState: State<ViewState> = _viewState

    override fun onServiceRegistered() {
        getDishesForCategory(category)
    }

    private fun getDishesForCategory(catName: String) {
        _viewState.value = ViewState.Loading
        coroutineScope.launch {
            try {
                val listDishes = usecase(catName)
                _viewState.value = ViewState.Success(listDishes)
            } catch (e: Exception) {
                Log.d("BK", "Exception ${e.message}")
                _viewState.value = ViewState.Error(e.message ?: "Unknown error sad times")
            }
        }
    }

    override fun onServiceUnregistered() {
        coroutineScope.cancel()
    }

    fun onDishClicked(dishId: String) {
        backstack.goTo(DetailScreen(dishId))
    }

}