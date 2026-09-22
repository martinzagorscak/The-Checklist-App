package com.example.thechecklistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.thechecklistapp.ui.navigation.SetupNavGraph
import com.example.thechecklistapp.ui.theme.TheChecklistAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheChecklistAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}
