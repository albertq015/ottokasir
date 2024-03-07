package com.ottokonek.ottokasir.dao.manager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class BluetoothManager {
    var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    lateinit var listener: BluetoothManagerListener
    lateinit var printer: com.ottokonek.ottokasir.utils.BluetoothUtil

    fun setPrinter(device: BluetoothDevice) {
        if (btAdapter == null) {
            listener.onNoBluetooth()
            return
        }
        printer = com.ottokonek.ottokasir.utils.BluetoothUtil(device)
        printer.connectPrinter(object : com.ottokonek.ottokasir.utils.BluetoothUtil.PrinterConnectListener {
            override fun onConnected() {
                listener.onConnected()
            }

            override fun onFailed() {
                listener.onFailed()
            }
        })
    }

    fun showPrinterSelectorDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pilih Printer")
        val devices: MutableList<BluetoothDevice> = ArrayList()
        devices.addAll(btAdapter!!.bondedDevices)

        if (devices.size == 0) {
            listener.onNoDevice()
            return
        }

        var arrays = arrayListOf<CharSequence>()

        for (dev: BluetoothDevice in devices) {
            arrays.add(dev.name)
        }


        var selected = 0

        builder.setSingleChoiceItems(arrays!!.toTypedArray(), selected, DialogInterface.OnClickListener { dialog, which ->
            selected = which
        })

        builder.setPositiveButton("Print", DialogInterface.OnClickListener { dialog, which ->
            listener.onDeviceSelected(devices.get(selected))
        })

        builder.setNegativeButton("Batal", null)
        val dialog = builder.create()
        dialog.show()
    }

    interface BluetoothManagerListener {
        fun onDeviceSelected(bluetoothDevice: BluetoothDevice)
        fun onConnected()
        fun onFailed()
        fun onNoDevice()
        fun onNoBluetooth()
    }


}