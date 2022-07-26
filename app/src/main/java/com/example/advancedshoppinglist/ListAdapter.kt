package com.example.advancedshoppinglist

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(
    private val context: Context,
    private val detailModels: ArrayList<ListModel>,
    private val clickListener: (ListModel) -> Unit

) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var onClickDeleteItem: ((ItemModel) -> Unit)? = null

    var logoLinks = mutableMapOf<String, String>(
        "Bread and Bakery" to "https://thumbs.dreamstime.com/b/breads-12481681.jpg",
        "Baking Ingredients" to "https://www.thespruceeats.com/thmb/hhx-2my0Glnj-6RaQcN7BFDfzOw=/4680x3120/filters:no_upscale()/greek-butter-cookies-1705307-step-01-5bfef717c9e77c00510e3bf9.jpg",
        "Meat and Seafood" to "https://thumbs.dreamstime.com/z/assortment-meat-seafood-beef-chicken-fish-pork-150220322.jpg",
        "Pasta and Rice" to "https://www.foodstoragemoms.com/wp-content/uploads/2015/07/Is-Freezing-Rice-And-Pasta-Okay1-1.jpg",
        "Oils, Sauces, Salad Dressings, and Condiments" to "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F44%2F2019%2F05%2F27225641%2Fsalad-dressing-for-diabetes.jpg&w=380&h=380&c=sc&poi=face&q=60",
        "Cereals and Breakfast Foods" to "https://www.bakeryandsnacks.com/var/wrbm_gb_food_pharma/storage/images/publications/food-beverage-nutrition/bakeryandsnacks.com/article/2020/02/25/there-s-still-a-place-on-the-table-for-sugary-breakfast-cereals/10741891-1-eng-GB/There-s-still-a-place-on-the-table-for-sugary-breakfast-cereals.jpg",
        "Soups and Canned Goods" to "https://www.tasteofhome.com/wp-content/uploads/2019/03/000_1400.jpg?fit=700,721",
        "Frozen Foods" to "https://thumbor.thedailymeal.com/-XpR5VRyFtnp-gWKle9_6Fbo2VU=/870x565/filters:focal(1000x642:1001x643)/https://www.thedailymeal.com/sites/default/files/story/2020/shutterstock_223395673.jpg",
        "Dairy, Cheese, and Eggs" to "https://media.meer.com/attachments/36283ad7e24b1ca11c6e04a3a9800f963c4d003b/store/fill/860/645/35defd11ef8de6b2a0af60645188fd44f1fdcc1e7ed397421e56f58cd7c7/Eggs-milk-and-cheese.jpg",
        "Snacks and Crackers" to "https://m.media-amazon.com/images/I/91GyvXT5vgL._SX425_.jpg",
        "Produce" to "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/La_Boqueria.JPG/1200px-La_Boqueria.JPG",
        "Drinks" to "https://5.imimg.com/data5/SELLER/Default/2020/12/BZ/RU/MR/117913229/carbonated-soft-drinks-500x500.jpg",
        "Candy" to "https://media.timeout.com/images/105705954/image.jpg",
        "Accessories" to "https://media.istockphoto.com/vectors/men-and-women-clothes-vector-icon-set-vector-id670999038",
        "Medicine" to "https://imgnew.outlookindia.com/public/uploads/articles/2020/4/1/rajasthans-free-medicine-scheme-secures-top-rank.jpg",
        "Cooking Supplies" to "https://ak1.ostkcdn.com/images/products/is/images/direct/412f01289eb757373b04b2548503e8d5226e608c/Kitchen-Utensil-set---Nylon---Stainless-Steel-Cooking---Baking-Supplies---Non-Stick-and-Heat-Resistant-Cookware-set---3-Sizes.jpg",
        "Sports and Outside Equipment" to "https://image.shutterstock.com/shutterstock/photos/1917840683/display_1500/stock-vector-sports-equipment-background-sport-concept-with-balls-and-gaming-items-balls-for-football-1917840683.jpg",
        "Toys" to "https://securecdn.pymnts.com/wp-content/uploads/2020/10/toys-spending-pandemic-Mattel.jpg",
        "Electronic Supplies" to "https://www.tds-office.com/wp-content/uploads/2018/10/office-electronics-printers-copiers.jpg",
        "Books" to "https://neatplaces.co.nz/media/thumbs/uploads/places/place/bay_hill_books/Close_up_of_modern_fiction_on_display_at_Bay_Hill_Books.jpg.650x425_q80_crop-smart_upscale.jpg",
        "Movies" to "https://i.ebayimg.com/images/g/YQUAAOSwttRf4mcL/s-l500.jpg",
        "Video Games and Supplies" to "https://cdn.arstechnica.net/wp-content/uploads/2021/11/dealmasterBF2021VG.jpg",
        "School Supplies" to "https://c8.alamy.com/comp/GBWW10/background-with-school-or-office-material-with-checkered-sheets-notebook-GBWW10.jpg",
        "House Supplies" to "https://image.shutterstock.com/image-vector/household-set-cleaning-supplies-icons-260nw-1276144684.jpg",
        "Tools" to "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/close-up-of-tools-hanging-on-wall-royalty-free-image-760251967-1563391812.jpg?crop=1.00xw:1.00xh;0,0&resize=640:*",
        "Garden Supplies" to "https://www.millcreekgardens.com/wp-content/uploads/2016/10/gardening-supplies-new.jpg",
        "Furniture" to "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/amazon-rivet-furniture-1533048038.jpg?crop=1.00xw:0.502xh;0,0.423xh&resize=1200:*",
        "Car Equipment" to "https://images.cars.com/cldstatic/wp-content/uploads/img-1282808648-1498163357413.jpg",
        "Juice" to "https://londonholisticdental.com/wp-content/uploads/2019/10/FruitJuices_Lead.jpg",
        "Other" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhNgUTM8IdFWa9wwCwVBkMy2l48qRSY_-pYCrsclOzK0yXBfWcm9ECldJMWKGhm-q-3o4&usqp=CAU")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_model, parent, false)
        return ViewHolder(view) {
            clickListener(detailModels[it])
        }
    }

    fun setOnClickDeleteItem(callback:(ItemModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return detailModels.size
    }

    // and we're going to define our own view holder which will encapsulate the memory card view
    inner class ViewHolder(itemView: View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }

        fun bind(position: Int) {
            val itemTv = itemView.findViewById<TextView>(R.id.itemId)
            val nameTv = itemView.findViewById<TextView>(R.id.idName)
            val storeTv = itemView.findViewById<TextView>(R.id.idStore)
            val typeTv = itemView.findViewById<TextView>(R.id.idType)
            val priceTv = itemView.findViewById<TextView>(R.id.idPrice)

            val logo = itemView.findViewById<ImageView>(R.id.imageView)

            val button = itemView.findViewById<Button>(R.id.btndelete)

            var model: ListModel = detailModels.get(position)


            val df = DecimalFormat("#.##")

            itemTv.setText("Item #: " + model.getId().toString().toLowerCase())
            nameTv.setText("Item Name: " + model.getName().toLowerCase())

            if (model.getStore() == " ") {
                storeTv.setText("Store: N/A")
            } else {
                storeTv.setText("Store: " + model.getStore().toLowerCase())
            }

            typeTv.setText("Item Type: " + model.getType())

            if (model.getPrice() == 0.0F) {
                priceTv.setText("Price: N/A")
            } else {
                priceTv.setText("Price: $" + df.format(model.getPrice()).toString())
            }


            /*
            val logoLinks = mutableMapOf<String, String>(
                "walmart" to "https://cdn.mos.cms.futurecdn.net/5StAbRHLA4ZdyzQZVivm2c-970-80.jpg.webp",
                "sobeys" to "https://upload.wikimedia.org/wikipedia/en/thumb/4/4f/Sobeys_logo.svg/1200px-Sobeys_logo.svg.png",
                "costco" to "https://p.kindpng.com/picc/s/146-1462162_empowering-marginalized-youth-in-the-outdoors-costco-high.png",
                "home depot" to "https://www.pngmart.com/files/16/Home-Depot-Logo-PNG-Photos.png",
                "best buy" to "https://corporate.bestbuy.com/wp-content/uploads/2018/05/2018_rebrand_blog_logo_LEAD_ART.jpg",
                "canadian tire" to "https://s22.q4cdn.com/405442328/files/design/CT-Brandmark-Standard-Secondary-RGW-POS-RGB.png",
                "superstore" to "https://upload.wikimedia.org/wikipedia/commons/8/8a/Real_Canadian_Superstore_logo.png",
                "real canadian superstore" to "https://upload.wikimedia.org/wikipedia/commons/8/8a/Real_Canadian_Superstore_logo.png",
                "canadian superstore" to "https://upload.wikimedia.org/wikipedia/commons/8/8a/Real_Canadian_Superstore_logo.png",
                "loblaws" to "https://creditlandingshoppingcentre.com/wp-content/uploads/2017/10/MASTER-RESIZED-LOGOS-Loblaws.jpg",
                "independent" to "https://www.tremblettsyig.com/images/Tremblett-Independent-Logo.png",
                "lowe's" to "https://logos-world.net/wp-content/uploads/2021/03/Lowes-Logo-700x394.png",
                "lowes" to "https://logos-world.net/wp-content/uploads/2021/03/Lowes-Logo-700x394.png",
                "nofrills" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTb7iBwmK6KyMubcwgngLLqd1ZUJI4wMJM7_pVub7FrSm71JvWT9FvtWKVW-iDJOY8ZBlE&usqp=CAU",
                "no frills" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTb7iBwmK6KyMubcwgngLLqd1ZUJI4wMJM7_pVub7FrSm71JvWT9FvtWKVW-iDJOY8ZBlE&usqp=CAU",
                "amazon" to "http://media.corporate-ir.net/media_files/IROL/17/176060/Oct18/Amazon%20logo.PNG",
                "rona" to "https://www.rona.ca/wcsstore/RONAStorefrontAssetStore/images/RONA_facebook_share.jpg",
                "freshco" to "http://images.pitchero.com/club_sponsors/17362/1557673930_large.jpg",
                "fresh co" to "http://images.pitchero.com/club_sponsors/17362/1557673930_large.jpg",
                "metro" to "https://torontoguardian.com/wp-content/uploads/2017/06/Metro-Logo.jpg",
                "farm boy" to "https://upload.wikimedia.org/wikipedia/en/thumb/7/77/Farm_Boy_logo.svg/330px-Farm_Boy_logo.svg.png",
                "farmboy" to "https://upload.wikimedia.org/wikipedia/en/thumb/7/77/Farm_Boy_logo.svg/330px-Farm_Boy_logo.svg.png") */

            /*
            Picasso.get().load(logoLinks.get(storeTv.text.toString().toLowerCase())).networkPolicy(
                NetworkPolicy.OFFLINE).into(logo) */

            Picasso.get().load(logoLinks.get(model.getType())).into(logo)

            button.setOnClickListener{
                onClickDeleteItem?.invoke(ItemModel(model.getId(), nameTv.text.toString(), storeTv.text.toString(), typeTv.text.toString(), model.getPrice()))
            }

            //var input: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
            //var output: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

            /*
            try {
                var t: Date = input.parse(model.getTime())
                timeTV.setText(output.format(t))
            } catch (e: ParseException) {
                e.printStackTrace()
            } */
        }

    }


}