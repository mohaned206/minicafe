package com.example.minicafe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.DeliveryDining
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minicafe.data.CoffeeRepository
import com.example.minicafe.data.CoffeeSize
import com.example.minicafe.data.toDt
import com.example.minicafe.ui.AppViewModel
import com.example.minicafe.ui.components.PrimaryButton
import com.example.minicafe.ui.components.QuantityStepper
import com.example.minicafe.ui.components.StatusBarIcons
import com.example.minicafe.ui.theme.Cream
import com.example.minicafe.ui.theme.Dark
import com.example.minicafe.ui.theme.DarkHeader
import com.example.minicafe.ui.theme.Grey
import com.example.minicafe.ui.theme.Primary
import com.example.minicafe.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    vm: AppViewModel,
    coffeeId: Int,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
) {
    StatusBarIcons(darkIcons = true)

    val coffee = remember(coffeeId) { CoffeeRepository.byId(coffeeId) }
    var size by remember { mutableStateOf(CoffeeSize.M) }
    var quantity by remember { mutableStateOf(1) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFavorite = vm.favorites.contains(coffee.id)
    val total = (coffee.basePrice + size.delta) * quantity

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Prix", fontSize = 12.sp, color = TextSecondary)
                        Text(total.toDt(), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                    }
                    PrimaryButton(
                        text = "Ajouter au panier",
                        onClick = {
                            vm.addToCart(coffee, size, quantity)
                            scope.launch {
                                snackbar.currentSnackbarData?.dismiss()
                                val result = snackbar.showSnackbar(
                                    message = "${coffee.name} ajouté au panier",
                                    actionLabel = "Voir",
                                    duration = SnackbarDuration.Short,
                                )
                                if (result == SnackbarResult.ActionPerformed) onOpenCart()
                            }
                        },
                        modifier = Modifier.width(217.dp),
                    )
                }
            }
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            // ---------- Barre du haut ----------
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .height(44.dp),
            ) {
                IconButton(onBack, Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", tint = Dark)
                }
                Text(
                    "Détail",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Dark,
                    modifier = Modifier.align(Alignment.Center),
                )
                IconButton({ vm.toggleFavorite(coffee.id) }, Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (isFavorite) Primary else Dark,
                    )
                }
            }

            Image(
                painterResource(coffee.detailImage),
                contentDescription = coffee.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(246.dp),
            )

            Column(Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(16.dp))

                // ---------- Titre + atouts ----------
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(coffee.name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Dark)
                        Text(coffee.subtitle, fontSize = 12.sp, color = TextSecondary)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, null, tint = Color(0xFFFBBE21), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("${coffee.rating}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FeatureIcon(Icons.Outlined.DeliveryDining)
                        FeatureIcon(Icons.Outlined.Coffee)
                        FeatureIcon(Icons.Outlined.WaterDrop)
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Grey)

                // ---------- Description ----------
                Text("Description", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Dark)
                Spacer(Modifier.height(8.dp))
                Text(coffee.description, fontSize = 12.sp, lineHeight = 21.sp, color = TextSecondary)

                // ---------- Taille ----------
                Spacer(Modifier.height(24.dp))
                Text("Taille", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Dark)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CoffeeSize.values().forEach { option ->
                        val active = option == size
                        Box(
                            Modifier
                                .weight(1f)
                                .height(41.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (active) DarkHeader else Color.White)
                                .border(1.dp, if (active) Primary else Grey, RoundedCornerShape(12.dp))
                                .clickable { size = option },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                option.label,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (active) Color.White else Dark,
                            )
                        }
                    }
                }

                // ---------- Quantité ----------
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Quantité",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Dark,
                        modifier = Modifier.weight(1f),
                    )
                    QuantityStepper(
                        quantity = quantity,
                        onMinus = { if (quantity > 1) quantity-- },
                        onPlus = { if (quantity < 20) quantity++ },
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FeatureIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Cream),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = Primary, modifier = Modifier.size(24.dp))
    }
}
