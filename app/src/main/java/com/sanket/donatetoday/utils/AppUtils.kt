package com.sanket.donatetoday.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.CreditCardDataDTO
import com.sanket.donatetoday.models.dto.UserDTO
import java.util.Locale


const val MaximumMonthlyGoal = 10000

fun String?.emptyIfNull() = this ?: ""

fun String?.getInitial(): String? = this?.getOrNull(0)?.toString()

fun String?.verifyEmptyOrNull() = isNullOrEmpty()

fun List<String>?.verifyEmptyOrNull() = isNullOrEmpty()
fun CreditCardDataDTO?.verifyEmptyOrNull(userType: String?) = (userType == UserType.Organization.type && (this?.cardHolderName.isNullOrEmpty() || this?.cardNo.isNullOrEmpty() || this?.expiresOn.isNullOrEmpty()))

fun String.isValidPassword(): Boolean {
    if (this.length < 8) return false
    if (this.firstOrNull { it.isDigit() } == null) return false
    if (this.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
    if (this.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
    if (this.firstOrNull { !it.isLetterOrDigit() } == null) return false

    return true
}

fun Location.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

fun getAddress(context: Context, latitude: Double, longitude: Double, onValueAccessed: (fullAddress: String, street: String?, city: String?, country: String?) -> Unit) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1) { addressList ->
                if (addressList != null && addressList.size > 0) {
                    val address = addressList[0]
                    val sb = StringBuilder()

                    for (i in 0 .. address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i))
                    }

                    onValueAccessed(sb.toString(), address.subLocality, address.locality, address.countryName)
                }
            }
        } else {
            val addressList =  geocoder?.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuilder()

                for (i in 0 .. address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i))
                }

                onValueAccessed(sb.toString(), address.subLocality, address.locality, address.countryName)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.bitmapDescriptorFromVector(@DrawableRes vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(this, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}