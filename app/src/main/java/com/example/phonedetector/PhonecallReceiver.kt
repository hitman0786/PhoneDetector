package com.example.phonedetector

import java.util.Date

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

abstract class PhonecallReceiver : BroadcastReceiver() {

    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var callStartTime: Date? = null
    private var isIncoming: Boolean = false
    private var savedNumber: String? = null

    override fun onReceive(context: Context, intent: Intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
        } else {
            val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var state = 0
            if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                state = TelephonyManager.CALL_STATE_IDLE
            } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                state = TelephonyManager.CALL_STATE_OFFHOOK
            } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                state = TelephonyManager.CALL_STATE_RINGING
            }


            onCallStateChanged(context, state, number)
        }
    }

    //Derived classes should override these to respond to specific events of interest
    abstract fun onIncomingCallStarted(ctx: Context, number: String?, start: Date)

    abstract fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date)
    abstract fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date)
    abstract fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date)
    abstract fun onMissedCall(ctx: Context, number: String?, start: Date?)

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    fun onCallStateChanged(context: Context, state: Int, number: String?) {
        /*if (lastState == state) {
            //No change, debounce extras
            return
        }*/

        callStartTime = Date()
        savedNumber = number

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                onIncomingCallStarted(context, number, callStartTime!!)
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    onOutgoingCallStarted(context, savedNumber, callStartTime!!)
                }
            TelephonyManager.CALL_STATE_IDLE ->
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime)
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }

}