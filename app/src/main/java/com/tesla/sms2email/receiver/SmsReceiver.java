package com.tesla.sms2email.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tesla.sms2email.global.Constants;
import com.tesla.sms2email.utils.SmtplUtils;
import com.tesla.sms2email.utils.SpUtils;


public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.TAG, "onReceive: ");
        SpUtils spUtils = new SpUtils(context);
        SmtplUtils smtpUtils = new SmtplUtils(spUtils);

        String title = Constants.TAG;
        StringBuilder content = new StringBuilder();
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String originatingAddress = smsMessage.getOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();
            Log.d(Constants.TAG, "onReceive: " + "originatingAddress: " +
                    originatingAddress + " messageBody: " + messageBody);

            title = originatingAddress;
            content.append(messageBody);
        }

        smtpUtils.sendEmail(title, content.toString());
    }

}
