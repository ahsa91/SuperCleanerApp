package my.supercleanerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * A data model class for reserved services with required fields.
 */

@Parcelize
data class ReservedService (
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    val reservation_id: String = "",
    val reservation_date: String = "",
    val reservation_time: String = "",
    val sub_total_amount: String = "",
    val total_amount: String = "",
    val address: Address = Address(),
    var id: String = "",
) : Parcelable
