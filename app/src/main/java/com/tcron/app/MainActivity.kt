package com.tcron.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.tcron.app.navigation.TCronNavigation
import com.tcron.app.ui.theme.TCronTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCronTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Show debug warning if in debug mode
                        if (BuildConfig.IS_DEBUG_BUILD) {
                            DebugWarningBanner()
                        }
                        
                        TCronNavigation(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun DebugWarningBanner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "DEBUG MODE ACTIVE",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.Red)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}