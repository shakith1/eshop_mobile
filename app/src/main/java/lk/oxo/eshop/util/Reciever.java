package lk.oxo.eshop.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

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
                        String messageBody = smsMessage.getMessageBody();

                        String otp = extractOTP(messageBody);
                        if (smsListener != null)
                            smsListener.onSmsReceived(otp);
                    }
                }
            }
        }
    }

    public String extractOTP(String message) {
        return message.substring(0, 6);
    }
}
