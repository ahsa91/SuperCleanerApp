package my.supercleanerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * A data model class for Reservation item with required fields.
 */
@Parcelize
data class Reservation(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val vat_charge: String = "",
    val total_amount: String = "",
    val reservation_date: String = "",
    val reservation_time: String = "",
    var id: String = ""
) : Parcelable
