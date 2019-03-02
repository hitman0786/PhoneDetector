package com.example.phonedetector

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.app_toolbar.*


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200
    var num = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        if(intent.getStringExtra("number") != null) {
            num = intent.getStringExtra("number")
        }

        if (num != "") {
            phoneET.setText(num)
        } else {
            phoneET.setText("State not found")
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

            if (!checkPermission()) {

                requestPermission();

            }
    }


    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS), PERMISSION_REQUEST_CODE)

    }
}
