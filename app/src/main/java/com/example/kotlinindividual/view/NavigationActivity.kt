package com.example.kotlinindividual.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinindividual.repository.ProductRepositoryImpl
import com.example.kotlinindividual.viewmodel.ProductViewModel


class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody() {

    data class BottomNavItem(val label: String, val icon: ImageVector)

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Search", Icons.Default.Search),
        BottomNavItem("Profile", Icons.Default.Person)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = context as Activity

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    val products by viewModel.allProducts.observeAsState(initial = emptyList())



    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }


    Scaffold(

        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }


        ,

        topBar = {
            TopAppBar(
                title = {
                    Text("Lugaloom",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp)
                },
                actions = {
                    IconButton(onClick =  {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }



                    IconButton(onClick =  {}) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick =  {}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },


        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddProductActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }





    )  {
            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            when (selectedIndex) {
                0 -> DashboardBody(viewModel = viewModel)
                1 -> Home2()
                2 -> Home3()
            }

        }
    }
}
@Composable
fun Home2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Search Screen")
    }
}


@Composable
fun Home3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile Screen")
    }
}


@Composable
fun DashboardBody(viewModel: ProductViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity


    val products = viewModel.allProducts.observeAsState(initial = emptyList())
    val loading = viewModel.loading.observeAsState(initial = true)

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (loading.value) {
            item {
                CircularProgressIndicator()
            }
        } else {
            items(products.value.size) { index ->
                val eachProduct = products.value[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {

                    Column(modifier = Modifier.padding(15.dp)) {
                        Text("Name: ${eachProduct?.productName ?: "N/A"}")
                        Text("Price: ${eachProduct?.productPrice ?: "N/A"}")
                        Text("Description: ${eachProduct?.productDescription ?: "N/A"}")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = { val intent=Intent(context,UpdateProductActivity::class.java)
                                          intent.putExtra("productId","${eachProduct?.productId}")
                                          context.startActivity(intent)
                                          }, colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.Gray
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }

                            IconButton(
                                onClick = {
                                    viewModel.deleteProduct(eachProduct?.productId.toString()) { success, message ->
                                        if (success) {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG)
                                                .show()
                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.Red
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)

                            }
                        }
                    }
                }

            }
        }
    }
}
@Preview
@Composable
fun PreviewNavigationActivity() {
    NavigationBody()
}

