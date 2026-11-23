package com.example.minicafe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minicafe.data.Category
import com.example.minicafe.data.Coffee
import com.example.minicafe.data.CoffeeSize
import com.example.minicafe.data.toDt
import com.example.minicafe.ui.AppViewModel
import com.example.minicafe.ui.SortOrder
import com.example.minicafe.ui.components.BottomNavBar
import com.example.minicafe.ui.components.StatusBarIcons
import com.example.minicafe.ui.components.toast
import com.example.minicafe.ui.theme.Dark
import com.example.minicafe.ui.theme.DarkHeader
import com.example.minicafe.ui.theme.Primary
import com.example.minicafe.ui.theme.ScreenBackground
import com.example.minicafe.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    vm: AppViewModel,
    onCoffeeClick: (Int) -> Unit,
    onOpenCart: () -> Unit,
) {
    StatusBarIcons(darkIcons = false)

    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val coffees = vm.visibleCoffees()
    val categories = remember { Category.values().toList() }
    var sortMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            BottomNavBar(
                cartCount = vm.cartCount,
                onCart = onOpenCart,
                onNotifications = { context.toast("Bientôt disponible") },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {

            // ---------- Header sombre + recherche ----------
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(DarkHeader)
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 28.dp),
            ) {
                Text("Bienvenue chez", fontSize = 11.sp, color = Color(0xFFA2A2A2))
                Spacer(Modifier.height(8.dp))
                Text("MiniCafé", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        value = vm.query,
                        onValueChange = { vm.query = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("Rechercher un café", fontSize = 12.sp, color = Color(0xFFA2A2A2)) },
                        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.White) },
                        trailingIcon = {
                            if (vm.query.isNotEmpty()) {
                                IconButton(onClick = { vm.query = "" }) {
                                    Icon(Icons.Filled.Close, "Effacer", tint = Color.White)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focus.clearFocus() }),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF2A2A2A),
                            unfocusedContainerColor = Color(0xFF2A2A2A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                    )

                    Box {
                        Box(
                            Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Primary)
                                .clickable { sortMenuOpen = true },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Filled.Tune, "Trier", tint = Color.White)
                        }
                        DropdownMenu(expanded = sortMenuOpen, onDismissRequest = { sortMenuOpen = false }) {
                            SortOrder.values().forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option.label,
                                            fontSize = 13.sp,
                                            fontWeight = if (vm.sort == option) FontWeight.SemiBold else FontWeight.Normal,
                                        )
                                    },
                                    trailingIcon = if (vm.sort == option) {
                                        { Icon(Icons.Filled.Check, null, tint = Primary) }
                                    } else null,
                                    onClick = {
                                        vm.sort = option
                                        sortMenuOpen = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            // ---------- Catégories ----------
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(categories) { category ->
                    val active = vm.category == category
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (active) Primary else Color.Transparent)
                            .clickable { vm.category = category }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        Text(
                            category.label,
                            fontSize = 12.sp,
                            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (active) Color.White else Dark,
                        )
                    }
                }
            }

            // ---------- Produits ----------
            if (coffees.isEmpty()) {
                Column(
                    Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(Icons.Outlined.SearchOff, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Aucun café trouvé", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Dark)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Essayez un autre mot-clé ou une autre catégorie.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    items(coffees, key = { it.id }) { coffee ->
                        CoffeeCard(
                            coffee = coffee,
                            onClick = {
                                focus.clearFocus()
                                onCoffeeClick(coffee.id)
                            },
                            onAdd = {
                                vm.addToCart(coffee, CoffeeSize.M, 1)
                                context.toast("${coffee.name} ajouté au panier")
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoffeeCard(coffee: Coffee, onClick: () -> Unit, onAdd: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Box(Modifier.fillMaxWidth().height(128.dp).clip(RoundedCornerShape(12.dp))) {
            Image(
                painterResource(coffee.image),
                contentDescription = coffee.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(bottomStart = 12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.Star, null, tint = Color(0xFFFBBE21), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("${coffee.rating}", fontSize = 10.sp, color = Color.White)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            coffee.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Dark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            coffee.subtitle,
            fontSize = 10.sp,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                coffee.basePrice.toDt(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Dark,
                modifier = Modifier.weight(1f),
            )
            Box(
                Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Primary)
                    .clickable(onClick = onAdd),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Add, "Ajouter au panier", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
