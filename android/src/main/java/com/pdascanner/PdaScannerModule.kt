package com.pdascanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule

class PdaScannerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val scannerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("PdaScannerModule", "Received intent: ${intent.action}")
            var barcode = intent.getStringExtra("Barcode") ?: intent.getStringExtra("barcode_data")
            if (barcode == null || barcode.isEmpty()) {
                val bundle = intent.extras
                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        val value = bundle.get(key)
                        if (value is String && value.isNotEmpty()) { barcode = value; break }
                        else if (value is ByteArray) { barcode = String(value); break }
                    }
                }
            }
            barcode = barcode ?: ""
            Log.e("PdaScannerModule", "Emitting to JS: $barcode")
            try {
                reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit("onBarcodeScanned", "${intent.action}:::$barcode")
            } catch (e: Exception) { Log.e("PdaScannerModule", "Failed to emit", e) }
        }
    }

    init {
        val filter = IntentFilter()
        filter.addAction("com.hc.scan")
        filter.addAction("com.escape.nomade.SCAN")
        filter.addAction("android.intent.ACTION_DECODE_DATA")
        filter.addAction("com.android.server.scannerservice.broadcast")
        filter.addAction("com.android.scanservice.scan.result")
        filter.addAction("nlscan.action.SCANNER_RESULT")
        filter.addAction("com.symbol.datawedge.api.RESULT_ACTION")
        filter.addAction("com.honeywell.action.PRINT_DATA")
        filter.addAction("com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED")
        filter.addAction("com.rfid.SCAN")
        filter.addAction("android.intent.action.SCANRESULT")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            reactApplicationContext.registerReceiver(scannerReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            reactApplicationContext.registerReceiver(scannerReceiver, filter)
        }
    }

    override fun getName(): String = "PdaScannerModule"
    
    override fun onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy()
        try { reactApplicationContext.unregisterReceiver(scannerReceiver) } catch (e: Exception) {}
    }

    @ReactMethod fun addListener(eventName: String) {}
    @ReactMethod fun removeListeners(count: Int) {}
}
