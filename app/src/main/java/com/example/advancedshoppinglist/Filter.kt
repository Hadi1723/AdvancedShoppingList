package com.example.advancedshoppinglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class Filter : AppCompatActivity() {

    companion object {
        private val TABLE_NAME = "list_items"
    }

    private var context: Context = this@Filter
    //declaring navigation bar
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var filter: EditText
    private lateinit var applyFilter: Button
    private lateinit var showFilterList: RecyclerView

    private lateinit var listOfItems: ArrayList<ListModel>
    private lateinit var itemAdapter: ListAdapter

    private lateinit var sqLiteHelper: SQLiteHelper


    private lateinit var showDetailedList: RecyclerView

    private lateinit var listOfDetails: ArrayList<DetailedInfoModel>
    private lateinit var detailAdapter: DetailedInfoAdapter

    private lateinit var filterHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        filter = findViewById(R.id.edtFilter)

        filterHeader = findViewById(R.id.filterHeading)

        applyFilter = findViewById(R.id.applyButton)
        applyFilter.setOnClickListener{

            if (filter.text.isEmpty()) {
                getAllItems("SELECT * FROM ${TABLE_NAME}", "", "id")
            }
            else if (filter.hint == "Enter Name of Item") {
                getAllItems("SELECT * FROM ${TABLE_NAME} WHERE name LIKE '%" + "${filter.text.toString().toLowerCase()}" + "%'", filter.text.toString(), "name")
            } else if (filter.hint == "Enter Name of Store") {
                getAllItems("SELECT * FROM ${TABLE_NAME} WHERE store LIKE '%" + "${filter.text.toString().toLowerCase()}" + "%'", filter.text.toString(), "store")
            } else if (filter.hint == "Enter Type of Item") {
                getAllItems("SELECT * FROM ${TABLE_NAME} WHERE type LIKE '%" + "${filter.text.toString().toLowerCase()}" + "%'", filter.text.toString(), "type")
            } else {
                getAllItems("SELECT * FROM ${TABLE_NAME} WHERE price <= ${filter.text.toString().toFloat()}", filter.text.toString(), "price")
            }
        }

        showFilterList = findViewById(R.id.showFilterList)

        listOfItems = arrayListOf<ListModel>()
        itemAdapter = ListAdapter(this, listOfItems) {

        }

        showFilterList.adapter = itemAdapter

        //filter.text.toString().toInt

        sqLiteHelper = SQLiteHelper(this)

        showDetailedList = findViewById(R.id.extraListInfo)


        //defining the navigation bar
        val drawerLayout: DrawerLayout =findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view2)
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getAllItems("SELECT * FROM ${TABLE_NAME}", "", "id")


        itemAdapter?.setOnClickDeleteItem {item -> ItemModel
            warnChange("Page Will Refresh If Deleted", null, View.OnClickListener {
                deleteItem(item.id)
                finish()
                startActivity(Intent(this, Filter::class.java))
            })

        }



        /*
        listOfDetails = arrayListOf(DetailedInfoModel(sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").sumPrice, "Total Cost", ""),
            DetailedInfoModel(sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").maxPrice, "Most Expensive Item", "Item Name: " + sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").maxPriceItem),
            DetailedInfoModel(sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").minPrice, "Least Expensive Item", "Item Name: " + sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").minPriceItem),
            DetailedInfoModel(sqLiteHelper.getData("SELECT * FROM ${TABLE_NAME}").avgPrice, "Least Expensive Item", "")

        )
        detailAdapter = DetailedInfoAdapter(this, listOfDetails)
        showDetailedList.adapter = detailAdapter


         */

        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.filterName-> {
                    //Toast.makeText(this, "Filter name", Toast.LENGTH_SHORT).show()
                    filterHeader.text = "Filter by Name"
                    filter.hint = "Enter Name of Item"

                }
                R.id.filterStore-> {
                    //Toast.makeText(this, "Filter Store", Toast.LENGTH_SHORT).show()
                    filterHeader.text = "Filter by Store"
                    filter.hint = "Enter Name of Store"
                }
                R.id.filterType-> {
                    //Toast.makeText(this, "Filter Type", Toast.LENGTH_SHORT).show()
                    filterHeader.text = "Filter by Type"
                    filter.hint = "Enter Type of Item"
                }
                R.id.filterPrice-> {
                    //Toast.makeText(this, "Filter Price", Toast.LENGTH_SHORT).show()
                    filterHeader.text = "Get items below inputted max price"
                    filter.hint = "Enter Maximum Price Limit"
                }

            }
            true


        }

    }

    private fun deleteItem(id: Int) {
        sqLiteHelper.deleteData(id)

        /*
        detailArrayList.filter {
            it.getId() == id
        }

        detailAdapter.notifyDataSetChanged() */


        for (i in 0..(listOfItems.size-1)) {
            if (listOfItems[i].getId() == id) {
                listOfItems.removeAt(i)
                itemAdapter.notifyDataSetChanged()
                break
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    //displaying the navigation bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    fun getAllItems(selectQuery: String, enteredText: String, columnName: String) {
        listOfItems.clear()

        val items = sqLiteHelper.getData(selectQuery).getItems()

        for (item in items) {
            val newItem = ListModel(item.id, item.name, item.store!!, item.type, item.price)

            /*
            if (columnName == "name" || columnName == "store" || columnName == "type") {
                Toast.makeText(this, "met condition", Toast.LENGTH_SHORT).show()
                if (item.name.contains(enteredText) || item.store.contains(enteredText) || item.type.contains(enteredText)) {
                    listOfItems.add(newItem)
                    continue
                }
            } */

            listOfItems.add(newItem)

        }

        itemAdapter.notifyDataSetChanged()

        listOfDetails = arrayListOf(DetailedInfoModel(sqLiteHelper.getData(selectQuery).sumPrice, "Total Cost", ""),
            DetailedInfoModel(sqLiteHelper.getData(selectQuery).maxPrice, "Most Expensive Item", "Item Name: " + sqLiteHelper.getData(selectQuery).maxPriceItem),
            DetailedInfoModel(sqLiteHelper.getData(selectQuery).minPrice, "Least Expensive Item", "Item Name: " + sqLiteHelper.getData(selectQuery).minPriceItem)

        )
        detailAdapter = DetailedInfoAdapter(this, listOfDetails)
        showDetailedList.adapter = detailAdapter
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