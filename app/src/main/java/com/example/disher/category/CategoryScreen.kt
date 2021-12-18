package com.example.disher.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.disher.category.usecase.IGetCategoriesUseCase
import com.example.disher.category.viewmodel.CategoryViewModel
import com.example.disher.ui.theme.*
import com.example.disher.ui.theme.Orange500
import com.example.disher.utils.noRippleClickable
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.get
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
class CategoryScreen(private val noArgsPlaceholder: String = "") : ComposeKey() {

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(CategoryViewModel(lookup(), backstack))
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewmodel = rememberService<CategoryViewModel>()
        val listOfCategories by remember { viewmodel.listOfCategories }

        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(listOfCategories) { item ->
                SingleItem(item.strCategory, item.strCategoryThumb) {
                    viewmodel.onItemClick(it)
                }
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleItem(
    title: String,
    thumbnail: String,
    onClick: (String) -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxWidth()
            .noRippleClickable { onClick(title) }
            .padding(12.dp)
    ){
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            val orangeYellowGradient = Brush.horizontalGradient(listOf(Orange500, Orange700), startX = 10f)
            Spacer(Modifier.height(30.dp))
            Box(
                modifier = Modifier.height(160.dp)
                    .fillMaxWidth()
                    .clip(Shapes.medium)
                    .background(orangeYellowGradient)
            ) {

            }
        }

        Box(
            modifier = Modifier.size(130.dp)
                .clip(RoundedCornerShape(65.dp))
                .background(Color.White),
        ){
            Image(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp)
        )
    }

}