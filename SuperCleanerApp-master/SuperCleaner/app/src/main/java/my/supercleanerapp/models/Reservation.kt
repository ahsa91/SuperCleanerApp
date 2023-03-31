package my.supercleanerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

///**
// * An enum class to represent the payment methods available for reservations.
// */
//enum class PaymentMethod {
//    CASH,
//    CARD
//}

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
    val reservation_status: Boolean = false,
    val reservation_paymentMethod: String="",
    var id: String = ""
) : Parcelable
