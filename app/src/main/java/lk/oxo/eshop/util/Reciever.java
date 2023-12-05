package lk.oxo.eshop.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import lk.oxo.eshop.R;

public class Reciever extends BroadcastReceiver {
    private SmsListener smsListener;

    public Reciever(SmsListener smsListener) {
        this.smsListener = smsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle data = intent.getExtras();
            System.out.println(intent.getAction());
            if (data != null) {
                Object[] pdus = (Object[]) data.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();

                        if(sender != null && sender.contains(context.getString(R.string.message_title))){
                            String otp = extractOTP(messageBody);
                            if (smsListener != null)
                                smsListener.onSmsReceived(otp);
                            break;
                        }
                    }
                }
            }
        }
    }

    public String extractOTP(String message) {
        return message.substring(0, 6);
    }
}
