package com.example.disher.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.disher.ComposeKey
import com.example.disher.R
import com.example.disher.detail.viewmodel.DetailViewModel
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.get
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class DetailScreen(private val mealId: String): ComposeKey() {

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder){
            add(DetailViewModel(lookup(), lookup(), mealId))
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewmodel = rememberService<DetailViewModel>()

        val singleDish by remember { viewmodel.meal }
        val uriHandler = LocalUriHandler.current

        singleDish?.let {
            Column(modifier = Modifier) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit,
                        painter = rememberImagePainter(
                            it.strMealThumb
                        ), contentDescription = null
                    )

                    Row(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        IconButton(onClick = {
                            uriHandler.openUri(it.strYoutube)
                        }) {
                            Icon(imageVector = Icons.Filled.PlayArrow,  contentDescription = null, tint = Color.White)
                        }
                        IconButton(onClick = {
                            viewmodel.saveToFavourites(it)
                        }) {
                            Icon(imageVector = Icons.Filled.Favorite,  contentDescription = null, tint = Color.Red)
                        }
                    }

                    Box(
                        modifier = Modifier.background((Color(0x4D000000)))
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ){
                        Column(
                            modifier = Modifier
                                .padding(12.dp)

                        ) {
                            Text(it.strMeal, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp))
                            Text(it.strArea, style = TextStyle(color = Color.White, fontSize = 14.sp))
                        }
                }


                }


                InstructionTextBlock(
                    instructions = it.strInstructions
                )

            }
        }
    }

}

fun Modifier.`if`(
    condition: Boolean,
    then: Modifier.() -> Modifier
): Modifier =
    if (condition) {
        then()
    } else {
        this
    }


@Composable
fun InstructionTextBlock(modifier: Modifier = Modifier, instructions: String) {
    var showmore by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)

    ) {
        Text(modifier = Modifier
            .`if`(!showmore) {
                height(100.dp)
            }
            .`if`(!showmore) {
                drawWithContent {
                    val colors = listOf(Color.Transparent, Color.White)
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors)
                    )
                }
            }, text = instructions
        )
        Button(onClick = {
            showmore = !showmore
        }) {
            Text(text = "Show More...")
        }

    }
}
