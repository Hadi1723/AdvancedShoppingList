package com.example.advancedshoppinglist

import kotlin.properties.Delegates

class ListModel {

    private var id by Delegates.notNull<Int>()
    private lateinit var name: String
    private lateinit var store: String
    private lateinit var type: String
    private var price: Float = 0.0F

    constructor(id: Int, name: String, store:String, type: String, price: Float) {
        this.id = id
        this.name = name
        this.store = store
        this.type = type
        this.price = price
    }

    @JvmName("getId1")
    fun getId(): Int {
        return this.id
    }

    @JvmName("setId1")
    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return this.name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getStore(): String {
        return this.store
    }

    fun setStore(store: String) {
        this.store = store
    }

    fun getType(): String {
        return this.type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getPrice(): Float {
        return this.price
    }

    fun setType(price: Float) {
        this.price = price
    }
}