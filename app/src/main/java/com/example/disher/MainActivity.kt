package com.example.disher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.disher.category.CategoryScreen
import com.example.disher.ui.theme.DisherTheme
import com.zhuinden.simplestack.AsyncStateChanger
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackcomposeintegration.core.BackstackProvider
import com.zhuinden.simplestackcomposeintegration.core.ComposeStateChanger
import com.zhuinden.simplestackextensions.navigatorktx.androidContentFrame
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider

class MainActivity : ComponentActivity() {

    private val composeStateChanger = ComposeStateChanger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = application as DisherApplication

        val backstack = Navigator.configure()
            .setStateChanger(AsyncStateChanger(composeStateChanger))
            .setScopedServices(DefaultServiceProvider())
            .setGlobalServices(application.globalServices)
            .install(this, androidContentFrame, History.of(CategoryScreen()))

        setContent {
            BackstackProvider(backstack) {
                DisherTheme {
                    Box(Modifier.fillMaxSize()) {
                        composeStateChanger.RenderScreen()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!Navigator.onBackPressed(this)) {
            super.onBackPressed()
        }
    }
}

//www.themealdb.com/api/json/v1/1/categories.php