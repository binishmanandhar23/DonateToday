package com.sanket.donatetoday.delegates

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.sanket.donatetoday.MainActivity
import com.sanket.donatetoday.models.dto.UserDTO


interface IntentDelegate {
    fun onEmail(activity: MainActivity, userDTO: UserDTO)

    fun onPhone(activity: MainActivity, userDTO: UserDTO)
}

class IntentDelegateImpl : IntentDelegate {
    override fun onEmail(activity: MainActivity, userDTO: UserDTO) {
        val intent = Intent(Intent.ACTION_SENDTO) // it's not ACTION_SEND
        intent.type = "message/rfc822"
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//        intent.putExtra(Intent.EXTRA_TEXT, body)
        intent.data = Uri.parse("mailto:${userDTO.emailAddress}") // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        activity.startActivity(intent)
    }

    override fun onPhone(activity: MainActivity, userDTO: UserDTO) {
        val intent = Intent(
            if (ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) Intent.ACTION_DIAL else Intent.ACTION_CALL,
            Uri.parse("tel:${userDTO.countryPhoneCode ?: ""}${userDTO.phoneNo}")
        )
        activity.startActivity(intent)
    }

}