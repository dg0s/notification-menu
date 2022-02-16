package org.vaadin.addons.dgos.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * A Designer generated component for the notification-button template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@NpmPackage(value = "moment", version = "^2.29.1")
@NpmPackage(value = "@polymer/paper-dialog", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-dialog-scrollable", version = "^3.0.1")
@Tag("notification-menu")
@JsModule("./notification-menu.ts")
public class NotificationMenu extends LitTemplate {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "YYYY/MM/DD HH:mm";
    public static final String DEFAULT_LABEL_MARK_ALL_AS_READ = "Mark all as read";
    public static final String DEFAULT_LABEL_VIEW_ALL = "View all";

    public NotificationMenu() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public void setOrientation(final Orientation orientation) {
        getElement().setProperty("orientation", orientation.getKey());
    }

    public void setCloseOnClick(final boolean closeOnClick) {
        getElement().setProperty("closeOnClick", closeOnClick);
    }

    public void setTitle(final String title) {
        getElement().setProperty("title", title);
    }

    public void setWidth(final String width) {
        getElement().setProperty("width", width);
    }

    public void setHeight(final String height) {
        getElement().setProperty("height", height);
    }

    public void setRingBellOn(final boolean ringBellOn) {
        getElement().setProperty("ringBell", ringBellOn);
    }

    public void open() {
        getElement().executeJs("open");
    }

    public void close() {
        getElement().executeJs("close");
    }

    public void setItems(NotificationItem... items) {
        setItems(Arrays.asList(items));
    }

    public void setItems(Collection<NotificationItem> items) {
        try {
            final String data = objectMapper.writeValueAsString(items);
            final JsonValue value = Json.instance().parse(data);
            getElement().setPropertyJson("notifications", value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the pattern used to format the date time of a notification item. By default "YYYY/MM/DD HH:mm".
     * <p></p>
     * Passing null will reset it to its default.
     * @param pattern pattern
     */
    public void setDateTimeFormatPattern(String pattern) {
        getElement().setProperty("dateTimeFormatPattern", pattern != null ? pattern : DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }

    /**
     * Returns the pattern used to format the date time of a notification item. By default "YYYY/MM/DD HH:mm".
     * @return date time format pattern
     */
    public String getDateTimeFormatPattern() {
        return getElement().getProperty("dateTimeFormatPattern", DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }

    /**
     * Sets the label to be used for the "View all" button in the popup. By default "View all".
     * <p></p>
     * Passing null will reset it to its default.
     * @param labelViewAll view all label
     */
    public void setLabelViewAll(String labelViewAll) {
        getElement().setProperty("labelViewAll", labelViewAll != null ? labelViewAll : DEFAULT_LABEL_VIEW_ALL);
    }

    /**
     * Returns the label to be used for the "View all" button in the popup. By default "View all".
     * @return view all label
     */
    public String getLabelViewAll() {
        return getElement().getProperty("labelViewAll", DEFAULT_LABEL_VIEW_ALL);
    }

    /**
     * Sets the label to be used for the "Mark all as read" button in the popup. By default "Mark all as read".
     * <p></p>
     * Passing null will reset it to its default.
     * @param labelMarkAllAsRead mark al as read label
     */
    public void setLabelMarkAllAsRead(String labelMarkAllAsRead) {
        getElement().setProperty("labelMarkAllAsRead", labelMarkAllAsRead != null ? labelMarkAllAsRead : DEFAULT_LABEL_MARK_ALL_AS_READ);
    }

    /**
     * Returns the label to be used for the "Mark as read" button in the popup. By default "Mark as read".
     * @return mark all as read label
     */
    public String getLabelMarkAllAsRead() {
        return getElement().getProperty("labelMarkAllAsRead", DEFAULT_LABEL_MARK_ALL_AS_READ);
    }


    public Registration addItemClickEventListener(ComponentEventListener<NotificationItemClickEvent> listener) {
        return addListener(NotificationItemClickEvent.class, listener);
    }

    public Registration addViewAllClickEventListener(ComponentEventListener<NotificationViewAllClickEvent> listener) {
        return addListener(NotificationViewAllClickEvent.class, listener);
    }

    public Registration addMarkAllAsReadClickEventListener(ComponentEventListener<NotificationMarkAllAsReadClickEvent> listener) {
        return addListener(NotificationMarkAllAsReadClickEvent.class, listener);
    }

    @DomEvent("view-all-clicked")
    public static class NotificationViewAllClickEvent extends ComponentEvent<NotificationMenu> {
        public NotificationViewAllClickEvent(NotificationMenu source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    @DomEvent("mark-all-clicked")
    public static class NotificationMarkAllAsReadClickEvent extends ComponentEvent<NotificationMenu> {
        public NotificationMarkAllAsReadClickEvent(NotificationMenu source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    @DomEvent("item-clicked")
    public static class NotificationItemClickEvent extends ComponentEvent<NotificationMenu> {
        private final NotificationItem selectedItem;

        public NotificationItemClickEvent(NotificationMenu source, boolean fromClient, @EventData("event.detail") JsonObject item) {
            super(source, fromClient);
            final NotificationType type = NotificationType.valueOf(item.get("type").asString().toUpperCase());
            final LocalDateTime datetime = LocalDateTime.parse(item.get("datetime").asString());
            this.selectedItem = new NotificationItem(type, item.get("key").asString(), item.get("title").asString(), item.get("description").asString(), datetime, item.get("read").asBoolean());
        }

        public NotificationItem getSelectedItem() {
            return selectedItem;
        }
    }
}
