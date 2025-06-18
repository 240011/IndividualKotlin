import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.kotlinindividual.view.ListViewActivity
import com.example.kotlinindividual.view.LoginActivity
import com.example.kotlinindividual.view.NavigationActivity
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashBody()
        }
    }

    @SuppressLint("commitPreEdits")
    @Composable
    fun SplashBody() {
        val context = LocalContext.current
        val activity = context as Activity

        val sharedPreferences = context.getSharedPreferences("User ", Context.MODE_PRIVATE)
        val localEmail: String = sharedPreferences.getString("email", "").toString()
        Log.d("email", localEmail)

        LaunchedEffect(Unit) {
            delay(3000) // Delay for 3 seconds
            if (localEmail.isEmpty()) {
                // Navigate to NewActivity if email is empty
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            } else {
                // Navigate to NavigationActivity if email is not empty
                val intent = Intent(context, NavigationActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            }
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
                // Display the app logo
                Image(
                    painter = painterResource(R.drawable.logo), // Ensure this is the correct drawable resource
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(150.dp) // Set the size of the logo
                        .padding(bottom = 16.dp) // Add some space below the logo
                )
                CircularProgressIndicator()
            }
        }
    }

    @Preview
    @Composable
    fun PreviewSplashActivity() {
        SplashBody()
    }
}
