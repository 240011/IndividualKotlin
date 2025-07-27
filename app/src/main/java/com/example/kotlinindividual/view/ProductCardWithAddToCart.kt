package com.example.kotlinindividual.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kotlinindividual.model.ProductModel
import com.example.kotlinindividual.repository.CartRepositoryImpl
import com.example.kotlinindividual.viewmodel.CartViewModel

@Composable
fun ProductCardWithAddToCart(
    product: ProductModel,
    modifier: Modifier = Modifier,
    onCartUpdated: () -> Unit = {}
) {
    val context = LocalContext.current
    val cartRepo = remember { CartRepositoryImpl() }
    val cartViewModel = remember { CartViewModel(cartRepo) }
    var quantity by remember { mutableStateOf(1) }
    var isAddingToCart by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product Image
            AsyncImage(
                model = product.image,
                contentDescription = product.productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(205.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.productName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Product Description
            Text(
                text = product.productDescription,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price
            Text(
                text = "$${String.format("%.2f", product.productPrice)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Quantity:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Decrease button
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    enabled = quantity > 1 && !isAddingToCart
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Quantity display
                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Increase button
                IconButton(
                    onClick = { quantity++ },
                    enabled = !isAddingToCart
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Total price for selected quantity
                Text(
                    text = "Total: $${String.format("%.2f", product.productPrice * quantity)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Add to Cart Button
                Button(
                    onClick = {
                        isAddingToCart = true
                        cartViewModel.addToCart(product, quantity) { success, message ->
                            isAddingToCart = false
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                quantity = 1 // Reset quantity after successful add
                                onCartUpdated() // Notify parent to update cart count
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isAddingToCart,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isAddingToCart) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adding...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Cart")
                    }
                }

                // View Cart Button
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, AddToCartActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(0.7f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Cart")
                }
            }
        }
    }
}
