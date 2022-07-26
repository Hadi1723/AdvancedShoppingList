package com.example.advancedshoppinglist

import kotlin.properties.Delegates

class DetailedInfoModel {

    private var quantity by Delegates.notNull<Float>()
    private lateinit var infoType: String
    private lateinit var itemName: String

    constructor(quantity: Float, infoType: String, itemName: String) {
        this.quantity = quantity
        this.infoType = infoType
        this.itemName = itemName
    }

    @JvmName("getQuantity1")
    fun getQuantity(): Float {
        return this.quantity
    }

    @JvmName("setQuantity1")
    fun setQuantity(quantity: Float) {
        this.quantity = quantity
    }

    fun getInfoType(): String {
        return this.infoType
    }

    fun setInfoType(infoType: String) {
        this.infoType = infoType
    }

    fun getItemName(): String {
        return this.itemName
    }

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

}