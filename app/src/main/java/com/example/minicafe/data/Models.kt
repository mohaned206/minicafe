package com.example.minicafe.data

import androidx.annotation.DrawableRes

enum class Category(val label: String) {
    ALL("Tous"),
    CAPUCIN("Capucin"),
    DIRECT("Direct"),
    EXPRESS("Express"),
    THE("Thé"),
}

enum class CoffeeSize(val label: String, val delta: Double) {
    S("S", -0.5),
    M("M", 0.0),
    L("L", 1.0),
}

data class Coffee(
    val id: Int,
    val name: String,
    val subtitle: String,
    val description: String,
    val basePrice: Double,
    val rating: Double,
    val category: Category,
    @DrawableRes val image: Int,
    @DrawableRes val detailImage: Int = image,
)

data class CartItem(
    val coffee: Coffee,
    val size: CoffeeSize,
    val quantity: Int,
) {
    val unitPrice: Double get() = coffee.basePrice + size.delta
    val total: Double get() = unitPrice * quantity
    val key: String get() = "${coffee.id}-${size.name}"
}

/** 4.0 -> "4 DT", 4.5 -> "4.5 DT" */
fun Double.toDt(): String {
    val rounded = Math.round(this * 10) / 10.0
    return if (rounded % 1.0 == 0.0) "${rounded.toInt()} DT" else "$rounded DT"
}
