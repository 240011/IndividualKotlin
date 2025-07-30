package com.example.kotlinindividual.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinindividual.R
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashBody()
        }
    }

    @Composable
    fun SplashBody() {
        val context = LocalContext.current
        val activity = context as Activity

        val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
        val localEmail = sharedPreferences.getString("email", "") ?: ""

        LaunchedEffect(Unit) {
            delay(3000)
            val target = if (localEmail.isEmpty()) {
                LoginActivity::class.java
            } else {
                NavigationActivity::class.java
            }
            context.startActivity(Intent(context, target))
            activity.finish()
        }

        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(Color.Yellow)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 16.dp)
                )
                CircularProgressIndicator()
            }
        }
    }

    @Preview
    @Composable
    fun PreviewSplashBody() {
        SplashBody()
    }
}
