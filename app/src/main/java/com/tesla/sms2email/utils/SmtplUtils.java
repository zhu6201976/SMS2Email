package com.tesla.sms2email.utils;

import android.util.Log;

import com.tesla.sms2email.global.Constants;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class SmtplUtils {
    private SpUtils spUtils;

    public SmtplUtils(SpUtils spUtils) {
        this.spUtils = spUtils;
    }

    public void sendEmail(final String title, final String Content) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String smtp_server = spUtils.getString(Constants.smtpServerKey, "");
                String email = spUtils.getString(Constants.emailKey, "");
                String password = spUtils.getString(Constants.passwordKey, "");

                Properties props = new Properties();
                props.put("mail.smtp.host", smtp_server);
                Session session = Session.getInstance(props, null);
                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(email);
                    msg.setRecipients(Message.RecipientType.TO, email);
                    msg.setSubject(title);
                    msg.setSentDate(new Date());
                    msg.setText(Content);
                    Transport.send(msg, email, password);
                    Log.d(Constants.TAG, "send success");
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.d(Constants.TAG, "send fail " + e);
                }
            }
        }).start();

    }

}
