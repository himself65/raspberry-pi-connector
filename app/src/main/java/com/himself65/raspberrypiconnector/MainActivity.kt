package com.himself65.raspberrypiconnector

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast

const val REQUEST_ENABLE_BT: Int = 1

class MainActivity : AppCompatActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var popupMenu: PopupMenu? = null

    private fun tryTurnOnBlueTooth() {
        if (this.bluetoothAdapter != null) {
            if (!this.bluetoothAdapter!!.isEnabled) {
                // support bluetooth
                Log.i("BLUETOOTH", "Support Bluetooth")
                // request to enable bluetooth
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).also { intent ->
                    Log.i("BLUETOOTH", "Enabled Bluetooth")
                    startActivityForResult(intent, REQUEST_ENABLE_BT)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "Failed to turn on the bluetooth.\n" +
                            "Please turn it on to make sure the app works well.",
                    Toast.LENGTH_SHORT
                )
                    .also { toast ->
                        Log.i("${toast.view.id}", "Not turn on bluetooth")
                    }
                    .show()
            }
        }
    }

    fun onMainButtonClick(view: View) {
        this.tryTurnOnBlueTooth()

        val popupMenu: PopupMenu =
            if (this.popupMenu == null) PopupMenu(this, view) else this.popupMenu as PopupMenu
        if (this.popupMenu == null) {
            this.bluetoothAdapter?.bondedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address  // Mac address
                popupMenu.menu.add("$deviceName $deviceHardwareAddress")
            }
            popupMenu.setOnMenuItemClickListener { item ->
                Log.i("POPUP_ITEM", item.toString())
                // todo: send message to target address
                true
            }
            this.popupMenu = popupMenu
        }
        popupMenu.show()
    }
}
