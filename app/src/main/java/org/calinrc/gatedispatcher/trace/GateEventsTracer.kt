package org.calinrc.gatedispatcher.trace

import android.content.Context
import android.util.Log
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class GateEventsTracer(val file: File, val dtf: DateTimeFormatter) {
    companion object {
        private var instance:GateEventsTracer? = null
        fun initInstance(context: Context){
            val loggingFile = File(context.filesDir, "logging.txt")
            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            instance = GateEventsTracer(loggingFile, dtf)
        }

        fun getInstance():GateEventsTracer{
            if(instance == null){
                throw IllegalArgumentException("Using n uninitialized logging system")
            }
                return instance as GateEventsTracer
        }
    }

    public fun addTrace(info:String){
        val now = LocalDateTime.now()
        val timeStr = this.dtf.format(now)
        val line = "$timeStr - $info\n"
        this.file.appendText(line)
    }
    fun getTrace():String{
        try {
            return this.file.readText()
        }catch (e:Exception){
            Log.e("GateEventsTracer", "Error reading trace file", e)
            return ""
        }
    }
    fun clearTrace(){
        this.file.writeText("")
    }
}