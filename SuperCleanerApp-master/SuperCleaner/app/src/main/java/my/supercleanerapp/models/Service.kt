package my.supercleanerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Service with required fields.
 */
@Parcelize
data class Service(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val image: String = "",
    var service_id: String = "",
) : Parcelable