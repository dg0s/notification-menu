package org.vaadin.addons.dgos.notification;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.internal.JsonSerializer;
import com.vaadin.flow.shared.Registration;
import elemental.json.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * A Designer generated component for the notification-button template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 * <p>
 * Items added to this menu are not cached inside, but seen as volatile. Events will recreate new instances
 * as needed. If you want to cache those items, you have to do that outside of this instance.
 */
@NpmPackage(value = "moment", version = "^2.29.1")
@NpmPackage(value = "@polymer/paper-dialog", version = "^3.0.1")
@NpmPackage(value = "@polymer/paper-dialog-scrollable", version = "^3.0.1")
@Tag("notification-menu")
@JsModule("./notification-menu.ts")
public class NotificationMenu extends LitTemplate implements HasSize {

    @Id("notification-header")
    private H4 header;

    @Id("view-all")
    private H4 viewAllButton;

    @Id("mark-all-as-read")
    private H4 markAllAsReadButton;

    private NotificationI18N i18n;

    /**
     * Sets the orientation of this component.
     * <p>
     * By default, the value is {@link Orientation#LEFT}.
     *
     * @param orientation the initial orientation of the pop-up
     */
    public void setOrientation(@NotNull final Orientation orientation) {
        getElement().setProperty("orientation", orientation.getKey());
    }

    /**
     * Sets whether the dialog can be closed by click outside the component.
     * <p>
     * By default, the dialog is closable when click outside the component.
     *
     * @param closeOnClick {@code true} to enable closing the dialog on click outside,
     *                     {@code false} to disable it
     */
    public void setCloseOnClick(final boolean closeOnClick) {
        getElement().setProperty("closeOnClick", closeOnClick);
    }

    /**
     * Sets the title of this component.
     * <p>
     * The title is shown in the dialog. If the title is <code>null</code> the component header is hidden.
     *
     * @param title the title value to set, or <code>null</code> to remove any
     *              previously set title
     * @deprecated use {@link #setI18n(NotificationI18N)} instead.
     */
    @Deprecated
    public void setTitle(final String title) {
        getElement().setProperty("title", title);
    }

    /**
     * Sets the maximal item count. When the item count is above that maximum, the count will be replaced
     * with the maxItemCountLabel. As soon as the item count is equal or below the maximum, it will show the
     * real count again.
     * <p>
     * The boolean parameter indicates, if the maximum item count label shall be updated automatically (true).
     * In that case the label will be based on the given max item count plus some "above" indicator.
     * <p>
     * Must be a number higher than 0.
     *
     * @param maxItemCount            maximal item count to show
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
     * Returns the maximal item count. By default, 99.
     *
     * @return max item count
     */
    public int getMaxItemCount() {
        return getElement().getProperty("maxItemCount", 99);
    }

    /**
     * Sets the maximal item count. When the item count is above that maximum, the count will be replaced
     * with the maxItemCountLabel. As soon as the item count is equal or below the maximum, it will show the
     * real count again.
     * <p>
     * Must be a number higher than 0.
     *
     * @param maxItemCount maximal item count to show
     */
    public void setMaxItemCount(final int maxItemCount) {
        setMaxItemCount(maxItemCount, false);
    }

    /**
     * Returns the placeholder / label that shall be shown, when the maximal item count is reached (i. e. greater than).
     *
     * @return max item count label
     */
    public String getMaxItemCountLabel() {
        return getElement().getProperty("maxItemCountLabel", "+" + getMaxItemCount());
    }

    /**
     * Sets a placeholder / label that shall be shown, when the maximal item count is reached (i. e. greater than).
     * Setting null will reset it to its default ("+" plus the current max item count).
     * <p>
     * You may set any string you like, but please remember, that it will be shown inside the small counter circle
     * and thus may exceed its boundaries.
     *
     * @param maxItemCountLabel max item count label
     */
    public void setMaxItemCountLabel(final String maxItemCountLabel) {
        getElement().setProperty("maxItemCountLabel", maxItemCountLabel != null ? maxItemCountLabel : "+" + getMaxItemCount());
    }

    /**
     * Sets whether the icon supports shake animation.
     * <p>
     *
     * @param ringBellOn {@code true} to enable shake animation on icon,
     *                   {@code false} to disable it
     * @deprecated use {@link #setAnimationEnable} instead.
     */
    @Deprecated
    public void setRingBellOn(final boolean ringBellOn) {
        setAnimationEnable(ringBellOn);
    }

    /**
     * Sets whether the icon supports shake animation.
     * <p>
     * By default, the icon animation is disabled.
     *
     * @param enable {@code true} to enable shake animation on icon,
     *               {@code false} to disable it
     */
    public void setAnimationEnable(final boolean enable) {
        getElement().setProperty("enableIconAnimation", enable);
    }

    /**
     * Opens the dialog.
     */
    public void open() {
        getElement().executeJs("open");
    }

    /**
     * Closes the dialog.
     */
    public void close() {
        getElement().executeJs("close");
    }

    public void setItems(NotificationItem... items) {
        setItems(Arrays.asList(items));
    }

    public void setItems(Collection<NotificationItem> items) {
        JsonArray jsonArray = JsonSerializer.toJson(items);
        JsonValue value = Json.instance().parse(jsonArray.toJson());
        getElement().setPropertyJson("notifications", value);
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
        JsonArray jsonArray = JsonSerializer.toJson(itemsToAdd);
        JsonValue value = Json.instance().parse(jsonArray.toJson());
        getElement().callJsFunction("addItems", value);
    }

    /**
     * Resend the given items to the client to update them. Items are mapped by their keys
     * ({@link NotificationItem#getKey()}).
     * <p>
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
     * <p>
     * Use this method if only a subset of items has changed.
     *
     * @param itemsToUpdate items to update
     */
    public void updateItems(Collection<NotificationItem> itemsToUpdate) {
        JsonArray jsonArray = JsonSerializer.toJson(itemsToUpdate);
        final JsonValue value = Json.instance().parse(jsonArray.toJson());
        getElement().callJsFunction("updateItems", value);
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
        JsonArray jsonArray = JsonSerializer.toJson(itemsToRemove);
        JsonValue value = Json.instance().parse(jsonArray.toJson());
        getElement().callJsFunction("removeItems", value);
    }

    /**
     * Returns the currently set icon. By default "vaadin:bell".
     *
     * @return icon
     */
    public String getIcon() {
        return getElement().getProperty("icon", "vaadin:bell");
    }

    /**
     * Sets a new icon. The icon name must consists of the icon family (e.g. "vaadin") and the icon
     * name (e.g. "bell"), separated by a colon, e.g. "vaadin:bell".
     *
     * @param icon icon
     */
    public void setIcon(String icon) {
        getElement().setProperty("icon", icon);
    }

    /**
     * Sets a new icon based on the given {@link VaadinIcon}.
     *
     * @param icon icon
     */
    public void setIcon(VaadinIcon icon) {
        // @see Icon.java
        setIcon("vaadin", icon.name().toLowerCase(Locale.ENGLISH).replace('_', '-'));
    }

    /**
     * Sets a new icon based on the given collection and name, for instance "vaadin" and "bell".
     *
     * @param collection collection
     * @param name       name
     */
    public void setIcon(String collection, String name) {
        setIcon(collection + ":" + name);
    }

    /**
     * Adds a click listener to an item of this component.
     * <p>
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    public Registration addItemClickEventListener(ComponentEventListener<NotificationItemClickEvent> listener) {
        return addListener(NotificationItemClickEvent.class, listener);
    }

    /**
     * Adds a click listener to 'view all' action of this component.
     * <p>
     *
     * @param listener listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    public Registration addViewAllClickEventListener(ComponentEventListener<NotificationViewAllClickEvent> listener) {
        return addListener(NotificationViewAllClickEvent.class, listener);
    }

    /**
     * Adds a click listener to 'mark all' action of this component.
     *
     * @param listener listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    public Registration addMarkAllAsReadClickEventListener(ComponentEventListener<NotificationMarkAllAsReadClickEvent> listener) {
        return addListener(NotificationMarkAllAsReadClickEvent.class, listener);
    }

    /**
     * Sets, if clicking on mark all as read shall automatically mark the client side items as read (true) or
     * keep them as they are (false). In both cases the {@link NotificationMarkAllAsReadClickEvent}
     * event is fired.
     * <p>
     * By default, clicking "mark all as read" only fires an event to the server. In that case the server
     * has to take care of mark all the items as read (e.g. by calling {@link #markAllAsRead() or handling
     * the items}.
     *
     * @param autoMarkAllAsRead automatically mark all items as read
     * @see #markAllAsRead()
     * @see NotificationMarkAllAsReadClickEvent
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
     *
     * @param itemsToMarkAsRead items to be marked as read
     */
    public void markAsRead(NotificationItem... itemsToMarkAsRead) {
        this.markAsRead(Arrays.asList(itemsToMarkAsRead));
    }

    /**
     * Marks the given items as "read" and informs the client.
     *
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

    /**
     * Sets the header visibility.
     * <p>
     * By default, the header is visible.
     *
     * @param visible {@code true} enable the header,
     *                {@code false} disable it
     */
    public void setHeaderVisible(boolean visible) {
        header.setVisible(visible);
    }

    /**
     * Sets the 'view all' action visibility.
     * <p>
     * By default, the action 'view all' is visible.
     *
     * @param visible {@code true} enable the 'view all' action,
     *                {@code false} disable it
     */
    public void setViewAllButtonVisible(boolean visible) {
        viewAllButton.setVisible(visible);
    }

    /**
     * Sets the 'mark all as read' action visibility.
     * <p>
     *
     * @param visible {@code true} enable the 'mark all as read' action,
     *                {@code false} disable it
     */
    public void setMarkAllAsReadButtonVisible(boolean visible) {
        markAllAsReadButton.setVisible(visible);
    }

    /**
     * Get the internationalization object previously set for this component.
     * <p>
     *
     * @return the object with the i18n properties. If the i18n properties
     *         weren't set, the object will return <code>null</code>.
     */
    public NotificationI18N getI18n() {
        return i18n;
    }

    /**
     * Set the internationalization properties for this component.
     *
     * @param i18n
     *            the internationalized properties, not <code>null</code>
     */
    public void setI18n(NotificationI18N i18n) {
        Objects.requireNonNull(i18n,
                "The I18N properties object should not be null");
        this.i18n = i18n;

        runBeforeClientResponse(ui -> {
            if (i18n == this.i18n) {
                setI18nWithJS();
            }
        });
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

    private void setI18nWithJS() {
        JsonObject i18nJson = (JsonObject) JsonSerializer.toJson(this.i18n);
        deeplyRemoveNullValuesFromJsonObject(i18nJson);
        getElement().callJsFunction("updateI18n", i18nJson);
    }

    private void deeplyRemoveNullValuesFromJsonObject(JsonObject jsonObject) {
        for (String key : jsonObject.keys()) {
            if (jsonObject.get(key).getType() == JsonType.OBJECT) {
                deeplyRemoveNullValuesFromJsonObject(jsonObject.get(key));
            } else if (jsonObject.get(key).getType() == JsonType.NULL) {
                jsonObject.remove(key);
            }
        }
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
