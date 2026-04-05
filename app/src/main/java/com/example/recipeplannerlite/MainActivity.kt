package com.example.recipeplannerlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipeplannerlite.HomeScreen.HomeScreen
import com.example.recipeplannerlite.ViewModel.HomeScreenViewModel
import com.example.recipeplannerlite.ui.theme.RecipePlannerLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val HomeScreenViewModel = HomeScreenViewModel()
        setContent {
            HomeScreen(viewModel = HomeScreenViewModel)
        }
    }

}