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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinindividual.R
import com.example.kotlinindividual.repository.ProductRepositoryImpl
import com.example.kotlinindividual.repository.CartRepositoryImpl
import com.example.kotlinindividual.viewmodel.ProductViewModel
import com.example.kotlinindividual.viewmodel.CartViewModel

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the selected tab from intent (if coming from cart)
        val selectedTab = intent.getIntExtra("selected_tab", 0)

        setContent {
            NavigationBody(initialSelectedIndex = selectedTab)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this activity
        val selectedTab = intent.getIntExtra("selected_tab", 0)
        setContent {
            NavigationBody(initialSelectedIndex = selectedTab)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody(initialSelectedIndex: Int = 0) {
    data class BottomNavItem(val label: String, val icon: ImageVector)

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Search", Icons.Default.Search),
        BottomNavItem("Categories", Icons.Default.List),
        BottomNavItem("Profile", Icons.Default.Person)
    )

    var selectedIndex by remember { mutableStateOf(initialSelectedIndex) }
    val context = LocalContext.current
    val activity = context as Activity

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    // Add cart functionality
    val cartRepo = remember { CartRepositoryImpl() }
    val cartViewModel = remember { CartViewModel(cartRepo) }
    val cartItems by cartViewModel.cartItems.observeAsState(initial = emptyList())

    val products by viewModel.allProducts.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
        cartViewModel.getCartItems()
    }

    // Refresh cart when returning from other activities
    LaunchedEffect(selectedIndex) {
        cartViewModel.getCartItems()
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
            },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.logo), // Replace with your logo resource
                                contentDescription = "Logo",
                                modifier = Modifier.size(40.dp) // Adjust size as needed
                            )
                            Text("Lugaloom",
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp)
                        }
                    },
                    actions = {
                        IconButton(onClick = { selectedIndex = 1 }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }

                        // Profile Icon
                        IconButton(onClick = {
                            val intent = Intent(context, EditProfileActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile"
                            )
                        }

                        // SHOPPING CART ICON WITH NAVIGATION TO CartScreen
                        IconButton(
                            onClick = {
                                selectedIndex = 1
                            }
                        ) {
                            BadgedBox(
                                badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge {
                                            Text(cartItems.size.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Shopping Cart - Click to view cart"
                                )
                            }
                        }

                        IconButton(onClick = {
                            Toast.makeText(context, "Settings clicked", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { activity.finish() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
    floatingActionButton = {
            if (selectedIndex == 0) { // Only show FAB on home screen
                FloatingActionButton(onClick = {
                    val intent = Intent(context, AddProductActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFE1F5FE)) // Set background color for all pages
        ) {
            when (selectedIndex) {
                0 -> DashboardBody(
                    viewModel = viewModel,
                    onCartUpdated = { cartViewModel.getCartItems() }
                )
                1 -> CartScreen(
                    viewModel = viewModel,
                    onCartUpdated = { cartViewModel.getCartItems() }
                )
                2 -> CategoriesScreen() // NEW: ListView functionality integrated here
                3 -> ProfileScreen()
            }
        }
    }
}

// NEW: Categories Screen with ListView functionality
@Composable
fun CategoriesScreen() {
    val context = LocalContext.current

    val images = listOf(
        R.drawable.tshirt,
        R.drawable.longcoat,
        R.drawable.sweater,
        R.drawable.pant,
        R.drawable.coat,
        R.drawable.jacket
    )
    val names = listOf(
        "T-Shirt",
        "LongCoat",
        "Sweater",
        "Pant",
        "Coat",
        "Jacket"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Product Categories",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Grid View",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyVerticalGrid(
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images.size) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // ✅ Navigate to AddToCartActivity on click
                                    val intent = Intent(context, AddToCartActivity::class.java)
                                    context.startActivity(intent)
                                    Toast.makeText(
                                        context,
                                        "${names[index]} clicked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(images[index]),
                                    contentDescription = names[index],
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(80.dp)
                                )
                                Text(
                                    text = names[index],
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Horizontal Scroll View",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(images.size) { index ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(context, AddToCartActivity::class.java)
                                    context.startActivity(intent)
                                    Toast.makeText(
                                        context,
                                        "${names[index]} clicked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        ) {
                            Image(
                                painter = painterResource(images[index]),
                                contentDescription = names[index],
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = names[index],
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    onCartUpdated: () -> Unit = {}
) {
    val products = viewModel.allProducts.observeAsState(initial = emptyList())
    val loading = viewModel.loading.observeAsState(initial = true)

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Browse Products",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (loading.value) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(products.value) { product ->
                    product?.let {
                        ProductCardWithAddToCart(
                            product = it,
                            onCartUpdated = onCartUpdated
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("User  profile and settings will be displayed here")

        // Edit Profile Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {
                        val intent = Intent(context, AddToCartActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "View Cart")
                    }

                    IconButton(onClick = {
                        val intent = Intent(context, AddProductActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Product")
                    }

                    // Edit Profile Button
                    IconButton(onClick = {
                        val intent = Intent(context, EditProfileActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }

                Text("Tap edit to modify your profile", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DashboardBody(
    viewModel: ProductViewModel,
    onCartUpdated: () -> Unit = {}
) {
    val context = LocalContext.current
    val products = viewModel.allProducts.observeAsState(initial = emptyList())
    val loading = viewModel.loading.observeAsState(initial = true)

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Product Management",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

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
                            Text("Price: $${String.format("%.2f", eachProduct?.productPrice ?: 0.0)}")
                            Text("Description: ${eachProduct?.productDescription ?: "N/A"}")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {
                                        val intent = Intent(context, UpdateProductActivity::class.java)
                                        intent.putExtra("productId", "${eachProduct?.productId}")
                                        context.startActivity(intent)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = Color.Gray
                                    )
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.deleteProduct(eachProduct?.productId.toString()) { success, message ->
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                            if (success) {
                                                viewModel.getAllProducts() // Refresh the list
                                            }
                                        }
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = Color.Red
                                    )
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
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
