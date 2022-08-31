package com.example.advancedshoppinglist

data class CurrentData(
    val listOfItems: ArrayList<ItemModel>,
    val latestId: Int,
    val sumPrice: Float,
    val maxPrice: Float,
    val maxPriceItem: String,
    val minPrice: Float,
    val minPriceItem: String) {
    fun getItems(): ArrayList<ItemModel> {
        return listOfItems
    }

    @JvmName("getLatestId1")
    fun getLatestId(): Int {
        return latestId
    }

    @JvmName("getSumPrice1")
    fun getSumPrice(): Float {
        return sumPrice
    }

    @JvmName("getMaxPrice1")
    fun getMaxPrice(): Float {
        return maxPrice
    }

    fun getMaxItem(): String {
        return maxPriceItem
    }

    @JvmName("getMinPrice1")
    fun getMinPrice(): Float {
        return minPrice
    }

    fun getMinItem(): String {
        return minPriceItem
    }

    /*
    @JvmName("getAvgPrice1")
    fun getAvgPrice(): Double {
        return avgPrice
    } */
}
