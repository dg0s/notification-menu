package org.vaadin.addons.dgos.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

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
     * Sets a new icon. The icon name must consists of the icon family (e.g. "vaadin") and the icon
     * name (e.g. "bell"), separated by a colon, e.g. "vaadin:bell".
     * @param icon icon
     */
    public void setIcon(String icon) {
        getElement().setProperty("icon", icon);
    }

    /**
     * Returns the currently set icon. By default "vaadin:bell".
     * @return icon
     */
    public String getIcon() {
        return getElement().getProperty("icon", "vaadin:bell");
    }

    /**
     * Sets a new icon based on the given collection and name, for instance "vaadin" and "bell".
     * @param collection collection
     * @param name name
     */
    public void setIcon(String collection, String name) {
        setIcon(collection + ":" + name);
    }

    /**
     * Sets a new icon based on the given {@link VaadinIcon}.
     * @param icon icon
     */
    public void setIcon(VaadinIcon icon) {
        // @see Icon.java
        setIcon("vaadin", icon.name().toLowerCase(Locale.ENGLISH).replace('_', '-'));
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
