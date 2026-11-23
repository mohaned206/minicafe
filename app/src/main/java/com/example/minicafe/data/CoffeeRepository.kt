package com.example.minicafe.data

import com.example.minicafe.R

/** Source de données locale. Plus tard : remplacer par Room ou une API. */
object CoffeeRepository {

    val coffees: List<Coffee> = listOf(
        Coffee(
            id = 1,
            name = "Capucin",
            subtitle = "Mousse légère",
            description = "Un espresso couronné d'une mousse de lait légère et veloutée, servi chaud ou froid.",
            basePrice = 4.0,
            rating = 4.8,
            category = Category.CAPUCIN,
            image = R.drawable.img_capucin,
            detailImage = R.drawable.img_detail,
        ),
        Coffee(
            id = 2,
            name = "Express",
            subtitle = "Serré et corsé",
            description = "Un café court et intense, préparé à la minute pour révéler toute la puissance du grain.",
            basePrice = 3.0,
            rating = 4.7,
            category = Category.EXPRESS,
            image = R.drawable.img_express,
        ),
        Coffee(
            id = 3,
            name = "Direct",
            subtitle = "Doux et crémeux",
            description = "Un café allongé au lait chaud, tout en douceur, pour bien démarrer la journée.",
            basePrice = 4.5,
            rating = 4.6,
            category = Category.DIRECT,
            image = R.drawable.img_mocha,
        ),
        Coffee(
            id = 4,
            name = "Thé à la menthe",
            subtitle = "Frais et parfumé",
            description = "Un thé vert infusé avec de la menthe fraîche, servi bien chaud dans un verre.",
            basePrice = 2.5,
            rating = 4.9,
            category = Category.THE,
            image = R.drawable.img_panna,
        ),
    )

    fun byId(id: Int): Coffee = coffees.firstOrNull { it.id == id } ?: coffees.first()
}
