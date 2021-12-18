package com.example.disher.dishes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.disher.ComposeKey
import com.example.disher.category.SingleItem
import com.example.disher.category.viewmodel.CategoryViewModel
import com.example.disher.dishes.model.DishesResponse
import com.example.disher.dishes.viewmodel.DishesViewModel
import com.example.disher.dishes.viewmodel.ViewState
import com.example.disher.ui.theme.Orange500
import com.example.disher.ui.theme.Orange700
import com.example.disher.ui.theme.Shapes
import com.example.disher.utils.noRippleClickable
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.get
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
class DishesScreen(private val category: String) : ComposeKey() {

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(DishesViewModel(lookup(), category, backstack))
        }
    }

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewmodel = rememberService<DishesViewModel>()
        val viewState by remember { viewmodel.viewState }

        when (val state = viewState) {
            is ViewState.Success -> {
                DishesList(state.data) {
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
                SingleDishItem(title = item.strMeal, thumbnail = item.strMealThumb) {
                    onDishClick(item.idMeal)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleDishItem(
    title: String,
    thumbnail: String,
    onClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .noRippleClickable { onClick() }
    ) {
        Column {
            val orangeYellowGradient =
                Brush.horizontalGradient(listOf(Orange500, Orange700), startX = 10f)
            Row(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .clip(Shapes.medium)
                    .background(orangeYellowGradient),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopStart),
                        painter = rememberImagePainter(
                            thumbnail
                        ), contentDescription = null
                    )
                }

                Text(
                    text = title,
                    fontSize = 17.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier.weight (1f)
                        .padding(horizontal = 16.dp)
                )
            }
        }

    }

}