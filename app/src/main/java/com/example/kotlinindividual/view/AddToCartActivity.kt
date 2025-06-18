//package com.example.kotlinindividual.view
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Remove
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import com.example.kotlinindividual.model.CartItemModel
//import com.example.kotlinindividual.model.ProductModel
//import com.example.kotlinindividual.repository.CartRepositoryImpl
//import com.example.kotlinindividual.viewmodel.CartViewModel
//
//class AddToCartActivity : ComponentActivity() {
//    private lateinit var product: ProductModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        // Get product data from intent
//        product = intent.getSerializableExtra("product") as? ProductModel
//            ?: ProductModel("", "Sample Product", 99.99, "Sample Description", "")
//
//        setContent {
//            AddToCartBody(product = product)
//        }
//    }
//}
//
//@Composable
//fun AddToCartBody(product: ProductModel) {
//    var quantity by remember { mutableStateOf(1) }
//    var specialInstructions by remember { mutableStateOf("") }
//
//    val repo = remember { CartRepositoryImpl() }
//    val viewModel = remember { CartViewModel(repo) }
//
//    val context = LocalContext.current
//    val activity = context as? Activity
//
//    val totalPrice = product.price * quantity
//
//    Scaffold { innerPadding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                // Product Image
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(250.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    if (product.imageUrl.isNotEmpty()) {
//                        AsyncImage(
//                            model = product.imageUrl,
//                            contentDescription = "Product Image",
//                            modifier = Modifier.fillMaxSize(),
//                            contentScale = ContentScale.Crop
//                        )
//                    } else {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "No Image Available",
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
//                    }
//                }
//            }
//
//            item {
//                // Product Details
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Text(
//                        text = product.name,
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Text(
//                        text = product.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//
//                    Text(
//                        text = "$${String.format("%.2f", product.price)}",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//
//            item {
//                // Quantity Selector
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        Text(
//                            text = "Quantity",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold
//                        )
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            IconButton(
//                                onClick = { if (quantity > 1) quantity-- },
//                                enabled = quantity > 1
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Remove,
//                                    contentDescription = "Decrease quantity"
//                                )
//                            }
//
//                            Text(
//                                text = quantity.toString(),
//                                style = MaterialTheme.typography.headlineMedium,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier.weight(1f)
//                            )
//
//                            IconButton(
//                                onClick = { quantity++ }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Add,
//                                    contentDescription = "Increase quantity"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            item {
//                // Special Instructions
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    label = { Text("Special Instructions (Optional)") },
//                    placeholder = { Text("Any special requests or notes...") },
//                    value = specialInstructions,
//                    onValueChange = { specialInstructions = it },
//                    minLines = 3,
//                    maxLines = 5
//                )
//            }
//
//            item {
//                // Price Summary
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                    )
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "Unit Price:",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                            Text(
//                                text = "$${String.format("%.2f", product.price)}",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "Quantity:",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                            Text(
//                                text = quantity.toString(),
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//
//                        Divider()
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "Total:",
//                                style = MaterialTheme.typography.titleLarge,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = "$${String.format("%.2f", totalPrice)}",
//                                style = MaterialTheme.typography.titleLarge,
//                                fontWeight = FontWeight.Bold,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                    }
//                }
//            }
//
//            item {
//                // Add to Cart Button
//                Button(
//                    onClick = {
//                        val cartItem = CartItemModel(
//                            id = "",
//                            productId = product.id,
//                            productName = product.name,
//                            productPrice = product.price,
//                            productImage = product.imageUrl,
//                            quantity = quantity,
//                            specialInstructions = specialInstructions,
//                            totalPrice = totalPrice
//                        )
//
//                        viewModel.addToCart(cartItem) { success, message ->
//                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//                            if (success) {
//                                activity?.finish()
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(
//                        text = "Add to Cart - $${String.format("%.2f", totalPrice)}",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//
//            item {
//                // Continue Shopping Button
//                OutlinedButton(
//                    onClick = {
//                        activity?.finish()
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(
//                        text = "Continue Shopping",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AddToCartBodyPreview() {
//    val sampleProduct = ProductModel(
//        id = "1",
//        name = "Sample Product",
//        price = 29.99,
//        description = "This is a sample product description for preview purposes.",
//        imageUrl = ""
//    )
//
//    AddToCartBody(product = sampleProduct)
//}