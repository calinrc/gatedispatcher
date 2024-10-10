package org.calinrc.gatedispatcher

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.ActivityCompat


class SmsReceiver : BroadcastReceiver() {
    companion object {
        private val TAG by lazy { SmsReceiver::class.java.simpleName }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "SmsReceiver - onReceive called")
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                for (pdu in pdus!!) {
                    val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val messageBody: String = smsMessage.messageBody
                    val phoneNumber: String = smsMessage.displayOriginatingAddress

                    // Check for specific text
                    if (messageBody.contains("SPECIFIC_TEXT")) {
                        initiateCall(context, "0740209686")
                    }
                }
            }
        }
    }

    private fun initiateCall(context: Context, phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.setData(Uri.parse("tel:$phoneNumber"))
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            return
        }
        context.startActivity(callIntent)
    }
}