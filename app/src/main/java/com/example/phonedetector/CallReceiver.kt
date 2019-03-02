package com.example.phonedetector

import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*


class CallReceiver : PhonecallReceiver() {

    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {

        val intent = Intent(ctx, MainActivity::class.java)
        intent.putExtra("number", number)
        ctx.startActivity(intent)
    }

    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {
       Log.e("TAG", "INCOMMIG")
    }

    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
        Log.e("TAG", "missed")

    }

    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        Log.e("TAG", "outend")
        val intent = Intent(ctx, MainActivity::class.java)
        intent.putExtra("number", number)
        ctx.startActivity(intent)
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {
        Log.e("TAG", "outstart")
    }
}