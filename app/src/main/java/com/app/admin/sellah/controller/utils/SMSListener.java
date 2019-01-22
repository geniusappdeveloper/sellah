package com.app.admin.sellah.controller.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSListener extends BroadcastReceiver {

    private static OTPListener mListener; // this listener will do the magic of throwing the extracted OTP to all the bound views.

    @Override
    public void onReceive(Context context, Intent intent) {

        // this function is trigged when each time a new SMS is received on device.

        Bundle data = intent.getExtras();

        Object[] pdus = new Object[0];
        if (data != null) {
            Log.e("OTP_DATA", "onReceive: "+data);
            pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
        }

        if (pdus != null) {
            for (Object pdu : pdus) { // loop through and pick up the SMS of interest
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.

                if (mListener!=null)
                    mListener.onOTPReceived("Extracted OTP");
                break;
            }
        }
    }

    public static void bindListener(OTPListener listener) {
        mListener = listener;
    }

    public static void unbindListener() {
        mListener = null;
    }
}