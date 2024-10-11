package org.calinrc.gatedispatcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import org.calinrc.gatedispatcher.trace.GateEventsTracer

class LoggingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logging_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val clearLoggingButton: Button = findViewById(R.id.button_clear_logs)
        clearLoggingButton.setOnClickListener {
            GateEventsTracer.getInstance().clearTrace()
            val viewLogs: TextView = findViewById(R.id.textViewLogs)
            viewLogs.text = GateEventsTracer.getInstance().getTrace()
        }
        val viewLogs: TextView = findViewById(R.id.textViewLogs)
        viewLogs.text =GateEventsTracer.getInstance().getTrace()
    }
}