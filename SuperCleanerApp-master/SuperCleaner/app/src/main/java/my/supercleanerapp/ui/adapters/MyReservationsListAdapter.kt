package my.supercleanerapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.supercleanerapp.R
import my.supercleanerapp.models.Reservation
import my.supercleanerapp.ui.activites.MyReservationDetailsActivity
import my.supercleanerapp.utils.Constants
import my.supercleanerapp.utils.GlideLoader

@Suppress("DEPRECATION")
open class MyReservationsListAdapter(
    private val context: Context,
    private var list: ArrayList<Reservation>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout_reservations_fragment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadServicePicture(model.image, holder.itemView.findViewById<ImageView>(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "€${model.total_amount}"


        }
        holder.itemView.setOnClickListener {
            // Launch service details screen.
            val intent = Intent(context, MyReservationDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_RESERVATIONS_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}