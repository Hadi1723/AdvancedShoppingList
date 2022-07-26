package com.example.advancedshoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        private val TABLE_NAME = "list_items"
    }

    private lateinit var currentID: TextView

    //declaring navigation bar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var buttonAdd: Button
    private lateinit var buttonView: Button
    private lateinit var buttonUpdate: Button
    private lateinit var sqliteHelper: SQLiteHelper

    private lateinit var edtName: EditText
    private lateinit var edtStore: EditText
    //private lateinit var edtType: EditText
    private lateinit var edtType: Spinner
    private lateinit var edtPrice: EditText

    var increasingName = 0
    var increasingStore = 0
    var increasingType = 0
    var increasingPrice = 0

    private lateinit var listRV: RecyclerView

    var id = 0


    //incorporating each item in recyclerview
    private lateinit var detailArrayList: ArrayList<ListModel>
    //incorporating the recyclerview item
    private lateinit var detailAdapter: ListAdapter

    private var dropDownList: ArrayList<String> = arrayListOf("(Mandatory) Enter Type Of Item")
    var itemType = dropDownList[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtName = findViewById(R.id.edtName)
        edtStore = findViewById(R.id.edtStore)
        edtType = findViewById(R.id.edtType)
        edtPrice = findViewById(R.id.edtPrice)
        currentID = findViewById(R.id.currentId)

        buttonUpdate = findViewById(R.id.btnUpdate)

        listRV = findViewById(R.id.listOfItems)

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,dropDownList)

        //defining list that contain each item in recylerview item
        detailArrayList = arrayListOf()

        //laying out the entire recyclerview
        detailAdapter = ListAdapter(this, detailArrayList) {
            edtName.setText(it.getName())
            edtStore.setText(it.getStore())
            edtPrice.setText(it.getPrice().toString())

            itemType = edtType.setSelection(adapter.getPosition(it.getType())).toString()

            id = it.getId()
            currentID.text = "Item #: " + it.getId().toString()
        }
        listRV.adapter = detailAdapter

        for (item in detailAdapter.logoLinks.keys) {
            dropDownList.add(item)
        }

        dropDownList.sort()

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        edtType.adapter = adapter
        edtType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //parent?.getItemAtPosition(dropDownList.indexOf(itemType))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.getItemAtPosition(position)

                if (position > 0) itemType = parent?.getItemAtPosition(position).toString()

            }


        }

        detailAdapter?.setOnClickDeleteItem { item -> ItemModel

            warnChange("Are You Sure You Want Delete the Item?", null, View.OnClickListener {
                deleteItem(item.id)
            })

        }

        buttonUpdate.setOnClickListener{
            update(id)
            viewResults()
        }

        //defining the navigation bar
        //val drawerLayout: DrawerLayout =findViewById(R.id.drawerLayout)
        //val navView: NavigationView = findViewById(R.id.nav_view)
        //toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        //drawerLayout.addDrawerListener(toggle)
        //toggle.syncState()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sqliteHelper = SQLiteHelper(this)

        buttonAdd = findViewById(R.id.btnAdd)
        buttonView = findViewById(R.id.btnView)


        buttonAdd.setOnClickListener{
            added()
        }

        buttonView.setOnClickListener{
            //val (listOfItems, numRows) = sqliteHelper.getAllStudent()

            viewResults()
        }


        /*
        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.orderName-> {
                    if (increasingName == 0) {
                        it.title = "Order by Name --"
                        increasingName++
                        detailArrayList.sortBy {
                            it.getName()
                        }
                    } else {
                        it.title = "Order by Name ++"
                        increasingName--
                        detailArrayList.sortByDescending {
                            it.getName()
                        }
                    }

                    detailAdapter.notifyDataSetChanged()
                }
                R.id.orderStore-> {
                    if (increasingStore == 0) {
                        it.title = "Order by Store --"
                        increasingStore++
                        detailArrayList.sortBy {
                            it.getStore()
                        }
                    } else {
                        it.title = "Order by Store ++"
                        increasingStore--
                        detailArrayList.sortByDescending {
                            it.getName()
                        }
                    }

                    detailAdapter.notifyDataSetChanged()
                }
                R.id.orderType-> {
                    if (increasingType == 0) {
                        it.title = "Order by Type --"
                        increasingType++
                        detailArrayList.sortBy {
                            it.getType()
                        }
                    } else {
                        it.title = "Order by Type ++"
                        increasingType--
                        detailArrayList.sortByDescending {
                            it.getType()
                        }
                    }

                    detailAdapter.notifyDataSetChanged()


                }
                R.id.orderPrice-> {
                    if (increasingPrice == 0) {
                        it.title = "Order by Price --"
                        increasingPrice++
                        detailArrayList.sortBy {
                            it.getPrice()
                        }
                    } else {
                        it.title = "Order by Price ++"
                        increasingPrice--
                        detailArrayList.sortByDescending {
                            it.getPrice()
                        }

                        detailAdapter.notifyDataSetChanged()
                    }

                }

            }

            true

        } */

    }

    private fun deleteItem(id: Int) {
        sqliteHelper.deleteData(id)

        /*
        detailArrayList.filter {
            it.getId() == id
        }

        detailAdapter.notifyDataSetChanged() */


        for (i in 0..(detailArrayList.size-1)) {
            if (detailArrayList[i].getId() == id) {
                detailArrayList.removeAt(i)
                detailAdapter.notifyDataSetChanged()
                break
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //displaying the navigation bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if(toggle.onOptionsItemSelected(item)){
          //  return true
        //}

        val intent = Intent(this, Filter::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    private fun added() {
        val name = edtName.text.toString()
        val store = edtStore.text.toString()
        val type = itemType

        if (name.isEmpty() || type == "(Mandatory) Enter Type Of Item") {
            Toast.makeText(this, "Enter information", Toast.LENGTH_SHORT).show()
        } else {
            val std: ItemModel

            id = sqliteHelper.getData("SELECT * FROM ${TABLE_NAME}").getItems().size + 1
            if (edtPrice.text.toString().isEmpty()) {

                if (store.isEmpty()) {
                    std = ItemModel(id = id, name = name,  type = type)
                } else {
                    std = ItemModel(id = id, name = name, store = store, type = type)
                }
            } else {
                val price = edtPrice.text.toString().toFloat()

                if (store.isEmpty()) {
                    std = ItemModel(id = id, name = name, type = type, price = price)
                } else {
                    std = ItemModel(id = id, name = name, store = store, type = type, price = price)
                }

            }
            val status = sqliteHelper.insertItem(std)

            if (status > -1) {
                Toast.makeText(this, "Item Added ", Toast.LENGTH_SHORT).show()
                currentID.text = "Item #: ${id}"

            }
        }
    }

    private fun viewResults() {

        detailArrayList.clear()

        var students = sqliteHelper.getData("SELECT * FROM ${TABLE_NAME}").getItems()

        if (students.isEmpty()) {
            Toast.makeText(this, "No values to show", Toast.LENGTH_SHORT).show()
        } else
        {
            for (student in students) {
                detailArrayList.add(ListModel(student.id,student.name,
                    student.store, student.type, student.price))
            }

            detailAdapter.notifyDataSetChanged()
        }

        //val intent = Intent(this, Filter::class.java)
        //startActivity(intent)
    }

    private fun update(id: Int) {
        val name = edtName.text.toString()
        val store: String = edtStore.text.toString()
        val type = itemType

        if (name.isEmpty() || type == "(Mandatory) Enter Type Of Item") {
            Toast.makeText(this, "Enter information", Toast.LENGTH_SHORT).show()
        } else {
            val std: ItemModel

            if (edtPrice.text.toString().isEmpty()) {

                if (store.isEmpty()) {
                    std = ItemModel(id, name = name,  type = type)
                } else {
                    std = ItemModel(id, name = name, store = store, type = type)
                }
            } else {
                val price = edtPrice.text.toString().toFloat()

                if (store.isEmpty()) {
                    std = ItemModel(id, name = name, type = type, price = price)
                } else {
                    std = ItemModel(id, name = name, store = store, type = type, price = price)
                }

            }
            val status = sqliteHelper.updateData(std)

            if (status > -1) {
                Toast.makeText(this, "Item Updated ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun warnChange(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok") {_, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

}