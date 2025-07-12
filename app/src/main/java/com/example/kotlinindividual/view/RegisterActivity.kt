package com.example.kotlinindividual.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.kotlinindividual.R
import com.example.kotlinindividual.model.UserModel
import com.example.kotlinindividual.repository.UserRepositoryImpl
import com.example.kotlinindividual.viewmodel.UserViewModel

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                RegisterBody(innerPadding)
            }
        }
    }
}

@Composable
fun RegisterBody(innerPaddingValues: PaddingValues) {
    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
    var firstName by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select Country") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    val options = listOf("Nepal", "India", "China")
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val context = LocalContext.current
    val activity = context as Activity

    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
            .padding(horizontal = 10.dp)
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // LOGO at the top
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Register",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = { Text("First Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                placeholder = { Text("Last Name") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@gmail.com") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedOptionText,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
                    .clickable { expanded = true },
                placeholder = { Text("Select Country") },
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Gray,
                    disabledContainerColor = Color.White
                ),
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOptionText = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Gender")
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = gender == "Male", onClick = { gender = "Male" })
            Text("Male", modifier = Modifier.padding(end = 10.dp))
            RadioButton(selected = gender == "Female", onClick = { gender = "Female" })
            Text("Female", modifier = Modifier.padding(end = 10.dp))
            RadioButton(selected = gender == "Others", onClick = { gender = "Others" })
            Text("Others")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it }
            )
            Text("I accept terms and conditions")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                userViewModel.register(email, password) { success, message, userId ->
                    if (success) {
                        val userModel = UserModel(
                            userId, email, firstName, lastname, gender, selectedOptionText, selectedOptionText
                        )
                        userViewModel.addUserToDatabase(userId, userModel) { successDb, msgDb ->
                            Toast.makeText(context, msgDb, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Centered "Already have an Account? Sign In"
        Text(
            text = "Already have an Account? Sign In",
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegPreview() {
    RegisterBody(innerPaddingValues = PaddingValues(0.dp))
}
