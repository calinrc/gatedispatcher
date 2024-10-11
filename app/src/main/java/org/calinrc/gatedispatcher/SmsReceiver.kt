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
import androidx.preference.PreferenceManager
import org.calinrc.gatedispatcher.trace.GateEventsTracer


class SmsReceiver : BroadcastReceiver() {
    companion object {
        private val TAG by lazy { SmsReceiver::class.java.simpleName }
        fun initiateCall(context: Context, phoneNumber: String) {
            Log.e(TAG, "SmsReceiver initiateCall on new coroutine")

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.setData(Uri.parse("tel:$phoneNumber"))
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission if not granted
                Log.e(TAG, "SmsReceiver has no CALL_PHONE permission")
            } else {
                Log.d(TAG, "SmsReceiver initiateCall startActivity")
                context.startActivity(callIntent)
            }

        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "SmsReceiver - onReceive called context:$context")

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val phoneNumber = sharedPreferences.getString("phone_number", "")
        val activationText = sharedPreferences.getString("activation_text", "")
        Log.d(TAG, "SmsReceiver - phoneNumber:$phoneNumber activationText:$activationText")

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                for (pdu in pdus!!) {
                    val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val messageBody: String = smsMessage.messageBody
                    val originalPhoneNumber: String = smsMessage.displayOriginatingAddress

                    // Check for specific text
                    if (!activationText.isNullOrEmpty() && !phoneNumber.isNullOrEmpty()) {
                        if (messageBody.trim().lowercase() == activationText.trim().lowercase()
                        ) {
                            Log.i(TAG, "SmsReceiver - initiateCall")
                            GateEventsTracer.getInstance().addTrace("Open Gate Processed - phoneNumber:$originalPhoneNumber")
                            initiateCall(context, phoneNumber)
                        } else {
                            Log.d(
                                TAG,
                                "SmsReceiver - ignore message:\"$messageBody\" from phoneNumber:$originalPhoneNumber"
                            )
                            GateEventsTracer.getInstance().addTrace("Open Gate Ignored - phoneNumber:$originalPhoneNumber message:\"$messageBody\"")
                        }
                    } else {
                        Log.e(TAG, "SmsReceiver - activationText or phoneNumber is null")
                    }
                }
            }
        }
    }
}