package org.calinrc.gatedispatcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import org.calinrc.gatedispatcher.trace.GateEventsTracer


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG by lazy { MainActivity::class.java.simpleName }
        private const val REQUEST_CODE_SMS_PERMISSIONS: Int = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(TAG, "onCreate called")
        requestSmsAndCallPermission()
        requestOverlayPermission()
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val phoneNumber = sharedPreferences.getString("phone_number", "")
        val activationText = sharedPreferences.getString("activation_text", "")
        GateEventsTracer.initInstance(applicationContext)
        if (phoneNumber.isNullOrEmpty() || activationText.isNullOrEmpty()) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        val gateButton: Button = findViewById(R.id.button_gate)

        gateButton.setOnClickListener {
            if (!phoneNumber.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    applicationContext.getString(R.string.gate_state_change_action_title),
                    Toast.LENGTH_SHORT
                ).show()
                GateEventsTracer.getInstance().addTrace("Manual initiated gate event")
                SmsReceiver.initiateCall(applicationContext, phoneNumber)
            }
        }
    }

    private fun requestSmsAndCallPermission() {
        val smsPermission = Manifest.permission.RECEIVE_SMS
        val callPermission = Manifest.permission.CALL_PHONE
        val grantSms = ContextCompat.checkSelfPermission(this, smsPermission)
        val grantCall = ContextCompat.checkSelfPermission(this, callPermission)
        if (grantSms != PackageManager.PERMISSION_GRANTED || grantCall != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(smsPermission, callPermission),
                REQUEST_CODE_SMS_PERMISSIONS
            )
        }
    }

    private fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) { // send user to the device settings
            val manageOverlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            Toast.makeText(
                this,
                applicationContext.getString(R.string.manage_overlay_permissions),
                Toast.LENGTH_SHORT
            ).show()
            startActivity(manageOverlayIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
                //findNavController(R.id.nav_host_fragment_content_main)
                true
            }
            R.id.action_logging -> {
                val intent = Intent(this, LoggingActivity::class.java)
                startActivity(intent)
                //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
                //findNavController(R.id.nav_host_fragment_content_main)
                true
            }

            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
                //findNavController(R.id.nav_host_fragment_content_main)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}