package com.example.disher.dishes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.disher.ComposeKey
import com.example.disher.category.SingleItem
import com.example.disher.category.viewmodel.CategoryViewModel
import com.example.disher.dishes.model.DishesResponse
import com.example.disher.dishes.viewmodel.DishesViewModel
import com.example.disher.dishes.viewmodel.ViewState
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.get
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
class DishesScreen(private val category: String) : ComposeKey() {

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(DishesViewModel(get(), category, backstack))
        }
    }

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewmodel = rememberService<DishesViewModel>()
        val viewState by remember { viewmodel.viewState }

        when (val state = viewState) {
            is ViewState.Success -> {
                DishesList(state.data){
                    viewmodel.onDishClicked(it)
                }
            }
            is ViewState.Error -> {
                Text(text = "Error:  ${state.errorMessage}")
            }
            else -> {
                Text(text = "Loading")
            }
        }
    }

}



@Composable
fun DishesList(dishes: DishesResponse, onDishClick: (String) -> Unit) {
    Column {
        LazyColumn {
            items(dishes.meals) { item ->
                SingleItem(title = item.strMeal, thumbnail = item.strMealThumb) {
                    onDishClick(item.idMeal)
                }
            }
        }
    }
}