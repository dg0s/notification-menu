package org.dgos.vaadin.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class NotificationItem {
    @JsonProperty
    private final String key;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String title;
    @JsonProperty
    private final String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty
    private final LocalDateTime datetime;
    @JsonProperty
    private boolean read;

    public NotificationItem(NotificationType type, String title, String description) {
        this(type, UUID.randomUUID().toString(), title, description, LocalDateTime.now(), false);
    }

    public NotificationItem(NotificationType type, String key, String title, String description, LocalDateTime datetime, boolean read) {
        Objects.requireNonNull(type, "NotificationItem 'type' cannot be null");
        Objects.requireNonNull(key, "NotificationItem 'key' cannot be null");
        Objects.requireNonNull(title, "NotificationItem 'title' cannot be null");
        this.key = key;
        this.type = type.name().toLowerCase();
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.read = read;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        read = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationItem that = (NotificationItem) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
