package com.ecotech.backend.notification;

import com.ecotech.backend.domain.User;

public interface NotificationChannel {

    String channelName();

    void send(User user, String title, String body);
}
