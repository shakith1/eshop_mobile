package lk.oxo.eshop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lk.oxo.eshop.R;

public class EmailSender extends AsyncTask<Void, Void, Void> {
    private String email;
    private String fname;
    private Context context;

    public EmailSender(String email,String fname,Context context) {
        this.email = email;
        this.context = context;
        this.fname = fname;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final String senderEmail = "8a59edd35fbef9";
        final String senderPassword = "929e9679f85798";

        Properties props = new Properties();
        props.put(context.getString(R.string.smtp_auth),context.getString(R.string.smtp_auth_value));
        props.put(context.getString(R.string.smtp_starttls),context.getString(R.string.smtp_starttls_value));
        props.put(context.getString(R.string.smtp_host),context.getString(R.string.smtp_host_value));
        props.put(context.getString(R.string.smtp_port),context.getString(R.string.smtp_port_value));

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail,
                               senderPassword);
                    }
                });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(context.getString(R.string.sender_email_address)));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(fname+context.getString(R.string.email_subject));

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_transparent);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            String template = EmailTemplate.getEmailTemplate(fname,base64Image);

            mimeMessage.setContent(template, "text/html; charset=utf-8");
            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}