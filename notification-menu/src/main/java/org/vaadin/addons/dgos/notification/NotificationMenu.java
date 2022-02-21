package org.vaadin.addons.dgos.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
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
 * <p></p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 * <p></p>
 * Items added to this menu are not cached inside, but seen as volatile. Events will recreate new instances
 * as needed. If you want to cache those items, you have to do that outside of this instance.
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

    @Id("view-all")
    private H4 viewAllButton;

    @Id("mark-all-as-read")
    private H4 markAllAsReadButton;

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

    /**
     * Sets the maximal item count. When the item count is above that maximum, the count will be repelaced
     * with the maxItemCountLabel. As soon as the item count is equal or below the maximum, it will show the
     * real count again.
     * <p></p>
     * Must be a number higher then 0.
     * @param maxItemCount maximal item count to show
     */
    public void setMaxItemCount(final int maxItemCount) {
        setMaxItemCount(maxItemCount, false);
    }

    /**
     * Sets the maximal item count. When the item count is above that maximum, the count will be repelaced
     * with the maxItemCountLabel. As soon as the item count is equal or below the maximum, it will show the
     * real count again.
     * <p></p>
     * The boolean parameter indicates, if the maximum item count label shall be updated automatically (true).
     * In that case the label will be based on the given max item count plus some "above" indicator.
     * <p></p>
     * Must be a number higher then 0.
     * @param maxItemCount maximal item count to show
     * @param updateMaxItemCountLabel update the maximum item count label
     */
    public void setMaxItemCount(final int maxItemCount, boolean updateMaxItemCountLabel) {
        if (maxItemCount < 1) {
            throw new IllegalArgumentException("Max item count needs to be a number > 0");
        }
        getElement().setProperty("maxItemCount", maxItemCount);
        if (updateMaxItemCountLabel) {
            setMaxItemCountLabel(null);
        }
    }

    /**
     * Returns the maximal item count. By default 99.
     * @return max item count
     */
    public int getMaxItemCount() {
        return getElement().getProperty("maxItemCount", 99);
    }

    /**
     * Sets a placeholder / label that shall be shown, when the maximal item count is reached (i. e. greater than).
     * Setting null will reset it to its default ("+" plus the current max item count).
     * <p></p>
     * You may set any string you like, but please remember, that it will be shown inside the small counter circle
     * and thus may exceed its boundaries.
     * @param maxItemCountLabel max item count label
     */
    public void setMaxItemCountLabel(final String maxItemCountLabel) {
        getElement().setProperty("maxItemCountLabel", maxItemCountLabel != null ? maxItemCountLabel : "+" + getMaxItemCount());
    }

    /**
     * Returns the placeholder / label that shall be shown, when the maximal item count is reached (i. e. greater than).
     * @return max item count label
     */
    public String getMaxItemCountLabel() {
        return getElement().getProperty("maxItemCountLabel", "+" + getMaxItemCount());
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
     * Adds the given items to this instance. Items are internally mapped by their keys
     * ({@link NotificationItem#getKey()}). Items already known to the client will be ignored.
     *
     * @param itemsToAdd items to add
     */
    public void addItems(NotificationItem... itemsToAdd) {
        this.addItems(Arrays.asList(itemsToAdd));
    }

    /**
     * Adds the given items to this instance. Items are internally mapped by their keys
     * ({@link NotificationItem#getKey()}). Items already known to the client will be ignored.
     *
     * @param itemsToAdd items to add
     */
    public void addItems(Collection<NotificationItem> itemsToAdd) {
        try {
            final String data = objectMapper.writeValueAsString(itemsToAdd);
            final JsonValue value = Json.instance().parse(data);
            getElement().callJsFunction("addItems", value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resend the given items to the client to update them. Items are mapped by their keys
     * ({@link NotificationItem#getKey()}).
     * <p></p>
     * Use this method if only a subset of items has changed.
     *
     * @param itemsToUpdate items to update
     */
    public void updateItems(NotificationItem... itemsToUpdate) {
        this.updateItems(Arrays.asList(itemsToUpdate));
    }

    /**
     * Resend the given items to the client to update them. Items are mapped by their keys
     * ({@link NotificationItem#getKey()}).
     * <p></p>
     * Use this method if only a subset of items has changed.
     *
     * @param itemsToUpdate items to update
     */
    public void updateItems(Collection<NotificationItem> itemsToUpdate) {
        try {
            final String data = objectMapper.writeValueAsString(itemsToUpdate);
            final JsonValue value = Json.instance().parse(data);
            getElement().callJsFunction("updateItems", value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes the given items from this instance. Items are internally mapped by their keys
     * ({@link NotificationItem#getKey()}). Items unknown to the client will be ignored.
     *
     * @param itemsToRemove items to remove
     */
    public void removeItems(NotificationItem... itemsToRemove) {
        this.removeItems(Arrays.asList(itemsToRemove));
    }

    /**
     * Removes the given items from this instance. Items are internally mapped by their keys
     * ({@link NotificationItem#getKey()}). Items unknown to the client will be ignored.
     *
     * @param itemsToRemove items to remove
     */
    public void removeItems(Collection<NotificationItem> itemsToRemove) {
        try {
            final String data = objectMapper.writeValueAsString(itemsToRemove);
            final JsonValue value = Json.instance().parse(data);
            getElement().callJsFunction("removeItems", value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the pattern used to format the date time of a notification item. By default "YYYY/MM/DD HH:mm".
     * <p></p>
     * Passing null will reset it to its default.
     *
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

    /**
     * Sets, if clicking on mark all as read shall automatically mark the client side items as read (true) or
     * keep them as they are (false). In both cases the {@link NotificationMarkAllAsReadClickEvent}
     * event is fired.
     * <p></p>
     * By default clicking "mark all as read" only fires an event to the server. In that case the server
     * has to take care of mark all the items as read (e.g. by calling {@link #markAllAsRead() or handling
     * the items}.
     *
     * @see #markAllAsRead()
     * @see NotificationMarkAllAsReadClickEvent
     *
     * @param autoMarkAllAsRead automatically mark all items as read
     */
    public void setAutoMarkAllAsRead(boolean autoMarkAllAsRead) {
        getElement().setProperty("autoMarkAllAsRead", autoMarkAllAsRead);
    }

    /**
     * This method will manually mark all known items on the client as read. Will not fire an extra event. Does
     * not modify the server side elements.
     */
    public void markAllAsRead() {
        getElement().callJsFunction("markAllAsRead");
    }

    /**
     * Marks the given items as "read" and informs the client.
     * @param itemsToMarkAsRead items to be marked as read
     */
    public void markAsRead(NotificationItem... itemsToMarkAsRead) {
        this.markAsRead(Arrays.asList(itemsToMarkAsRead));
    }

    /**
     * Marks the given items as "read" and informs the client.
     * @param itemsToMarkAsRead items to be marked as read
     */
    public void markAsRead(Collection<NotificationItem> itemsToMarkAsRead) {
        itemsToMarkAsRead.forEach(NotificationItem::markAsRead);
        updateItems(itemsToMarkAsRead);
    }


    /**
     * This method will trigger the client side as if the user would have clicked the
     * "view all" button including a client side based event.
     */
    public void viewAll() {
        getElement().callJsFunction("_onViewAll");
    }

    /**
     * Clears the list without firing any event nor closing the dialog.
     */
    public void clearAll() {
        setItems();
    }

    public void setViewAllButtonVisible(boolean visible) {
        viewAllButton.setVisible(visible);
    }

    public void setMarkAllAsReadButtonVisible(boolean visible) {
        markAllAsReadButton.setVisible(visible);
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
