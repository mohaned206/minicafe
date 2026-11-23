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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minicafe.data.CartItem
import com.example.minicafe.data.toDt
import com.example.minicafe.ui.AppViewModel
import com.example.minicafe.ui.components.AppTextField
import com.example.minicafe.ui.components.PrimaryButton
import com.example.minicafe.ui.components.QuantityStepper
import com.example.minicafe.ui.components.StatusBarIcons
import com.example.minicafe.ui.theme.Danger
import com.example.minicafe.ui.theme.Dark
import com.example.minicafe.ui.theme.Grey
import com.example.minicafe.ui.theme.Primary
import com.example.minicafe.ui.theme.ScreenBackground
import com.example.minicafe.ui.theme.TextSecondary

@Composable
fun CartScreen(
    vm: AppViewModel,
    onBack: () -> Unit,
    onBrowse: () -> Unit,
    onOrderDone: () -> Unit,
) {
    StatusBarIcons(darkIcons = true)

    var showConfirmation by remember { mutableStateOf(false) }
    var confirmedTotal by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .height(44.dp),
            ) {
                IconButton(onBack, Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", tint = Dark)
                }
                Text(
                    "Commande",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Dark,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        },
        bottomBar = {
            if (vm.cart.isNotEmpty()) {
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
                            Text("Total", fontSize = 12.sp, color = TextSecondary)
                            Text(vm.total.toDt(), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                        }
                        PrimaryButton(
                            text = "Commander",
                            onClick = {
                                if (vm.delivery && vm.address.isBlank()) {
                                    addressError = "Entrez votre adresse de livraison"
                                } else {
                                    confirmedTotal = vm.total.toDt()
                                    showConfirmation = true
                                }
                            },
                            modifier = Modifier.width(217.dp),
                        )
                    }
                }
            }
        },
    ) { padding ->
        if (vm.cart.isEmpty() && !showConfirmation) {
            EmptyCart(Modifier.padding(padding), onBrowse)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { DeliveryToggle(vm) }

                if (vm.delivery) {
                    item {
                        Column {
                            Text("Adresse de livraison", fontSize = 12.sp, color = Dark)
                            Spacer(Modifier.height(6.dp))
                            AppTextField(
                                value = vm.address,
                                onValueChange = {
                                    vm.address = it
                                    addressError = null
                                },
                                placeholder = "Rue, ville",
                                error = addressError,
                                imeAction = ImeAction.Done,
                            )
                        }
                    }
                }

                items(vm.cart.toList(), key = { it.key }) { item ->
                    CartRow(
                        item = item,
                        onMinus = { vm.changeQuantity(item, -1) },
                        onPlus = { vm.changeQuantity(item, +1) },
                        onRemove = { vm.remove(item) },
                    )
                }

                item { Summary(vm) }
            }
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Commande confirmée ✅", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    if (vm.delivery) "Merci ! Votre commande de $confirmedTotal est en préparation et sera livrée à l'adresse indiquée."
                    else "Merci ! Votre commande de $confirmedTotal est en préparation. Vous pouvez venir la récupérer."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    vm.clearCart()
                    onOrderDone()
                }) { Text("OK", color = Primary, fontWeight = FontWeight.SemiBold) }
            },
        )
    }
}

@Composable
private fun DeliveryToggle(vm: AppViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEDEDED))
            .padding(4.dp),
    ) {
        listOf(true to "Livraison", false to "À emporter").forEach { (isDelivery, label) ->
            val active = vm.delivery == isDelivery
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (active) Primary else Color.Transparent)
                    .clickable { vm.delivery = isDelivery }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    fontSize = 14.sp,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (active) Color.White else Dark,
                )
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource(item.coffee.image),
            contentDescription = item.coffee.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.coffee.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark)
            Text(
                "Taille ${item.size.label}  •  ${item.unitPrice.toDt()}",
                fontSize = 11.sp,
                color = TextSecondary,
            )
            Spacer(Modifier.height(8.dp))
            QuantityStepper(item.quantity, onMinus, onPlus)
        }
        Column(horizontalAlignment = Alignment.End) {
            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.Delete, "Supprimer", tint = Danger, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(item.total.toDt(), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark)
        }
    }
}

@Composable
private fun Summary(vm: AppViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text("Résumé", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Dark)
        Spacer(Modifier.height(12.dp))
        SummaryLine("Sous-total", vm.subtotal.toDt())
        Spacer(Modifier.height(8.dp))
        SummaryLine("Livraison", if (vm.deliveryFee == 0.0) "Gratuit" else vm.deliveryFee.toDt())
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Grey)
        SummaryLine("Total", vm.total.toDt(), bold = true)
    }
}

@Composable
private fun SummaryLine(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            label,
            fontSize = 13.sp,
            color = if (bold) Dark else TextSecondary,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )
        Text(
            value,
            fontSize = 13.sp,
            color = Dark,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
private fun EmptyCart(modifier: Modifier, onBrowse: () -> Unit) {
    Column(
        modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.ShoppingBag, null, tint = TextSecondary, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(16.dp))
        Text("Votre panier est vide", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Dark)
        Spacer(Modifier.height(4.dp))
        Text(
            "Ajoutez vos cafés préférés pour commencer.",
            fontSize = 12.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Découvrir le menu", onBrowse, modifier = Modifier.width(220.dp))
    }
}
