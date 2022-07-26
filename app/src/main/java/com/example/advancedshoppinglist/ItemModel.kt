package com.example.advancedshoppinglist

import java.util.*

data class ItemModel (val id: Int = getAutoID(), val name: String, val store: String = " ", val type: String, val price: Float = 0.0F) {
    companion object {
        fun getAutoID(): Int {
            /*
            val random = (0..100000).random()
            return random */
            val random = Random()
            return random.nextInt(1000000)
        }
    }
}