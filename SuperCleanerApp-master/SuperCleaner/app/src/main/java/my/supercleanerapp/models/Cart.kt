package my.supercleanerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val user_id: String = "",
    val service_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    val cart_quantity: String = "",
    var id: String = "",
) : Parcelable