package com.ecotech.backend.notification;

import com.ecotech.backend.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("sms")
public class SmsNotificationChannel implements NotificationChannel {

    @Value("${app.africastalking.username:#{null}}")
    private String username;

    @Value("${app.africastalking.api-key:#{null}}")
    private String apiKey;

    @Override
    public String channelName() {
        return "sms";
    }

    @Override
    public void send(User user, String title, String body) {
        if (username == null || apiKey == null || user.getPhone() == null) {
            return;
        }
        System.out.println("[SMS] to=" + user.getPhone() + " | " + title + " | " + body);
    }
}
