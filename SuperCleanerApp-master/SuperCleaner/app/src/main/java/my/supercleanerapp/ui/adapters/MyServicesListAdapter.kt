package my.supercleanerapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.supercleanerapp.R
import my.supercleanerapp.utils.GlideLoader
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.fragments.ServicesFragment

@Suppress("DEPRECATION")
open class MyServicesListAdapter(
    private val context: Context,
    private var list: ArrayList<Service>,
    private val fragment: ServicesFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
// END

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadServicePicture(model.image, holder.itemView.findViewById<ImageView>(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "$${model.price}"

//            // TODO Step 4: Assigning the click event to the delete button.
//            // START
//            holder.itemView.findViewById<TextView>(R.id.ib_delete_product).setOnClickListener {
//
//                // TODO Step 8: Now let's call the delete function of the ProductsFragment.
//                // START
//                fragment.deleteProduct(model.product_id)
//                // END
//            }
//            // END
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}