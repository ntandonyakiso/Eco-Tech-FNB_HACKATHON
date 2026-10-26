package com.ecotech.backend.notification;

import com.ecotech.backend.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class NotificationService {

    private final Map<String, NotificationChannel> channels;

    public NotificationService(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(java.util.stream.Collectors.toMap(NotificationChannel::channelName, c -> c));
    }

    public void notify(User user, List<String> channelNames, String title, String body) {
        for (String name : channelNames) {
            NotificationChannel channel = channels.get(name);
            if (channel != null) {
                channel.send(user, title, body);
            }
        }
    }

    public void notify(User user, String channelName, String title, String body) {
        NotificationChannel channel = channels.get(channelName);
        if (channel != null) {
            channel.send(user, title, body);
        }
    }
}
