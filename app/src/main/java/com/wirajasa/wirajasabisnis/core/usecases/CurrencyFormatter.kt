package com.wirajasa.wirajasabisnis.core.usecases

import java.text.DecimalFormat

class CurrencyFormatter {

    operator fun invoke(text: String): String {
        val price: Double = text.toDouble()
        val formatter = DecimalFormat("###,###,###")
        val newPrice = formatter.format(price)
        return "Rp $newPrice"
    }
}