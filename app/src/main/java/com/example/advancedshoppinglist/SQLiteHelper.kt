package com.example.advancedshoppinglist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "shopping.db"
        private val DATABASE_VERSION = 3
        private val TABLE_NAME = "list_items"
        private val ID = "id"
        private val NAME = "name"
        private val STORE = "store"
        private val TYPE = "type"
        private val PRICE = "price"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        // Constraints
        /*

        1. PRIMARY KEY columns can be used to uniquely identify the row. Attempts to insert a row with an identical value to a row already in the table will result in a constraint violation which will not allow you to insert the new row.

        2. UNIQUE columns have a different value for every row. This is similar to PRIMARY KEY except a table can have many different UNIQUE columns.

        3. NOT NULL columns must have a value. Attempts to insert a row without a value for a NOT NULL column will result in a constraint violation and the new row will not be inserted.

        4. DEFAULT columns take an additional argument that will be the assumed value for an inserted row if the new row does not specify a value for that column.

        Example:
         CREATE TABLE celebs (
            id INTEGER PRIMARY KEY,
            name TEXT UNIQUE,
            date_of_birth TEXT NOT NULL,
            date_of_death TEXT DEFAULT 'Not Applicable'
         );

         */

        val createTBlStudent = ("CREATE TABLE " + TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY," + NAME
                + " TEXT," + STORE + " TEXT," +
                TYPE + " TEXT," + PRICE + " FLOAT" + ")")
        db?.execSQL(createTBlStudent)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun insertItem(itd: ItemModel): Long {

        val dp = this.writableDatabase

        //val numRows = getData("SELECT * FROM $TABLE_NAME").getLatestId() + 1



        val contentValues = ContentValues()
        contentValues.put(ID,itd.id)
        contentValues.put(NAME, itd.name.toLowerCase())
        contentValues.put(STORE, itd.store.toLowerCase())
        contentValues.put(TYPE, itd.type)
        contentValues.put(PRICE, itd.price)

        // The INSERT statement inserts a new row into a table.
        //parts of insert statement
        // clause: INSERT INTO ____ VALUES
        // table: table name
        // parameters: (the column names) and (the specfic values into each column)
        val success = dp.insert(TABLE_NAME, null, contentValues)
        dp.close()
        return success

    }

    @SuppressLint("Range")
    fun getData(selectQuery: String): CurrentData {
        val stdList: ArrayList<ItemModel> = arrayListOf()

        //val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase

        val cursor: Cursor?

        //db.query()

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch(e: Exception) {
            db.execSQL(selectQuery)
            e.printStackTrace()
            return CurrentData(arrayListOf(), 0, 0.0F, 0.0F, "", 0.0F, "")
        }

        var id: Int
        var name: String
        var store: String
        var type: String
        var price: Float

        var itemNum: Int = 1

        var sumPrice: Float = 0.0F

        var listOfItems = mutableMapOf<Float, String>()

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                store = cursor.getString(cursor.getColumnIndex("store"))
                type = cursor.getString(cursor.getColumnIndex("type"))
                price = cursor.getFloat(cursor.getColumnIndex("price"))

                val std = ItemModel(id, name, store, type, price)
                stdList.add(std)

                //rowsNum++
                sumPrice += price

                listOfItems.put(price, name)

                itemNum++


            } while(cursor.moveToNext())
        } else {
            return CurrentData(stdList, 0, 0.0F, 0.0F,
                "", 0.0F,
                "")
        }

        /*
        var avgPrice: Double = listOfPrices.average()

        listOfItems.maxOf { it.key }

        listOfItems.keys.average()

        listOfItems.maxOf {
            it.value
        }

        listOfItems.minOf {
            it.value
        } */

        return CurrentData(stdList, id, sumPrice, listOfItems.maxOf { it.key },
            listOfItems.get(listOfItems.maxOf { it.key }).toString(), listOfItems.minOf { it.key },
            listOfItems.get(listOfItems.minOf { it.key }).toString()
        )
    }

    fun updateData(itd: ItemModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, itd.id)
        contentValues.put(NAME, itd.name)
        contentValues.put(STORE, itd.store)
        contentValues.put(TYPE, itd.type)
        contentValues.put(PRICE, itd.price)

        val success = db.update(TABLE_NAME, contentValues, "id= " + "${itd.id}" + "", null)

        db.close()

        return success

    }

    fun deleteData(id: Int): Int {
        val db = this.writableDatabase

        //need these otherwise no output
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TABLE_NAME,"id= $id", null)

        db.close()

        return success
    }

    /*
    @SuppressLint("Range")
    fun getNumberOfRows(): Int? {
        val db = this.readableDatabase

        var value: Int?

        var selectQuery: String = "SELECT * FROM $TABLE_NAME"

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: Exception) {
            db.execSQL(selectQuery)
            value = -100
        }

        value = cursor?.getInt(cursor?.getColumnIndex("id"))

        return value
    } */
}