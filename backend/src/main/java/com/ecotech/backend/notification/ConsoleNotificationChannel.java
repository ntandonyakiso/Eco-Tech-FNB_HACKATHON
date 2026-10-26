package com.ecotech.backend.notification;

import com.ecotech.backend.domain.User;
import org.springframework.stereotype.Component;

@Component("console")
public class ConsoleNotificationChannel implements NotificationChannel {

    @Override
    public String channelName() {
        return "console";
    }

    @Override
    public void send(User user, String title, String body) {
        System.out.println("[NOTIFICATION] to=" + user.getEmail() != null ? user.getEmail() : user.getPhone()
                + " | " + title + " | " + body);
    }
}
