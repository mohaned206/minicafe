package com.example.minicafe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.minicafe.data.CartItem
import com.example.minicafe.data.Category
import com.example.minicafe.data.Coffee
import com.example.minicafe.data.CoffeeRepository
import com.example.minicafe.data.CoffeeSize
import java.text.Normalizer

enum class SortOrder(val label: String) {
    DEFAULT("Par défaut"),
    PRICE_ASC("Prix croissant"),
    PRICE_DESC("Prix décroissant"),
    RATING("Mieux notés"),
}

/** "Thé" et "the" doivent matcher. */
private fun String.normalized(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()

class AppViewModel : ViewModel() {

    // ---------- Recherche / filtres ----------
    var query by mutableStateOf("")
    var category by mutableStateOf(Category.ALL)
    var sort by mutableStateOf(SortOrder.DEFAULT)

    fun visibleCoffees(): List<Coffee> {
        val q = query.trim().normalized()
        val filtered = CoffeeRepository.coffees.filter { coffee ->
            val matchCategory = category == Category.ALL || coffee.category == category
            val matchQuery = q.isEmpty() ||
                coffee.name.normalized().contains(q) ||
                coffee.subtitle.normalized().contains(q)
            matchCategory && matchQuery
        }
        return when (sort) {
            SortOrder.DEFAULT -> filtered
            SortOrder.PRICE_ASC -> filtered.sortedBy { it.basePrice }
            SortOrder.PRICE_DESC -> filtered.sortedByDescending { it.basePrice }
            SortOrder.RATING -> filtered.sortedByDescending { it.rating }
        }
    }

    // ---------- Favoris ----------
    val favorites = mutableStateListOf<Int>()

    fun toggleFavorite(id: Int) {
        if (!favorites.remove(id)) favorites.add(id)
    }

    // ---------- Panier ----------
    val cart = mutableStateListOf<CartItem>()
    var delivery by mutableStateOf(true)
    var address by mutableStateOf("")

    val cartCount: Int get() = cart.sumOf { it.quantity }
    val subtotal: Double get() = cart.sumOf { it.total }
    val deliveryFee: Double get() = if (delivery && cart.isNotEmpty()) DELIVERY_FEE else 0.0
    val total: Double get() = subtotal + deliveryFee

    fun addToCart(coffee: Coffee, size: CoffeeSize, quantity: Int = 1) {
        val index = cart.indexOfFirst { it.coffee.id == coffee.id && it.size == size }
        if (index >= 0) {
            cart[index] = cart[index].copy(quantity = cart[index].quantity + quantity)
        } else {
            cart.add(CartItem(coffee, size, quantity))
        }
    }

    fun changeQuantity(item: CartItem, delta: Int) {
        val index = cart.indexOfFirst { it.key == item.key }
        if (index < 0) return
        val newQuantity = cart[index].quantity + delta
        if (newQuantity <= 0) cart.removeAt(index)
        else cart[index] = cart[index].copy(quantity = newQuantity)
    }

    fun remove(item: CartItem) {
        cart.removeAll { it.key == item.key }
    }

    fun clearCart() {
        cart.clear()
    }

    companion object {
        const val DELIVERY_FEE = 2.0
    }
}
