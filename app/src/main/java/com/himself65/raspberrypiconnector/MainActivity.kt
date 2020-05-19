package com.himself65.raspberrypiconnector

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var popupMenu: PopupMenu? = null

    companion object {
        @JvmStatic
        var REQUEST_ENABLE_BT: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (this.bluetoothAdapter != null) {
            // support bluetooth
            Log.i("BLUETOOTH", "Support Bluetooth")
            if (!this.bluetoothAdapter!!.isEnabled) {
                // request to enable bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
    }

    final fun onMainButtonClick(view: View) {
        val popupMenu: PopupMenu =
            if (this.popupMenu == null) PopupMenu(this, view) else this.popupMenu as PopupMenu
        if (this.popupMenu == null) {
            popupMenu.inflate(R.menu.popup_selector_menu)
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
        }
        popupMenu.show()

        this.popupMenu = popupMenu
    }
}
