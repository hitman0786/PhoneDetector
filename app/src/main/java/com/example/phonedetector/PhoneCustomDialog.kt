package com.example.phonedetector

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.dialog_fetch_user_data.*

class PhoneCustomDialog: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setFinishOnTouchOutside(false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fetch_user_data)

        okBT.setOnClickListener {
            if(android.os.Build.VERSION.SDK_INT >= 21)
            {
                finishAndRemoveTask();
            }
            else
            {
                finish();
            }
        }
    }
}