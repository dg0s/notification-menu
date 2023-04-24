package org.vaadin.addons.dgos.notification;

import java.io.Serializable;

public class NotificationI18N implements Serializable {
    private String title;
    private String markAllAsRead;
    private String viewAll;
    private String dateTimeFormatPattern;

    public NotificationI18N() {
    }

    /**
     * Returns the title of notification header. By default, "Notifications".
     *
     * @return header title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title for the notification header. By default, "Notifications".
     *
     * @param title 'Notifications' title.
     * @return self
     */
    public NotificationI18N setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Returns the caption for 'Mark All As Read' action. By default, "Mark All As Read".
     *
     * @return caption of 'Mark All As Read' action.
     */
    public String getCaptionMarkAllAsRead() {
        return this.markAllAsRead;
    }

    /**
     * Sets the caption for 'Mark All As Read' action. By default, "Mark All as Read".
     *
     * @param caption 'Mark All As Read' caption.
     * @return self
     */
    public NotificationI18N setCaptionMarkAllAsRead(String caption) {
        this.markAllAsRead = caption;
        return this;
    }

    /**
     * Returns the caption for 'View All' action. By default, "View All".
     *
     * @return caption of 'View All' action
     */
    public String getCaptionViewAll() {
        return this.viewAll;
    }

    /**
     * Sets the caption for 'View All' action. By default, "View All".
     *
     * @param caption 'View All' caption
     * @return self
     */
    public NotificationI18N setCaptionViewAll(String caption) {
        this.viewAll = caption;
        return this;
    }

    /**
     * Returns the pattern used to format the date time of a notification item. By default "YYYY/MM/DD HH:mm".
     *
     * @return date time format pattern
     */
    public String getDateTimeFormatPattern() {
        return dateTimeFormatPattern;
    }

    /**
     * Sets the pattern used to format the date time of a notification item. By default "YYYY/MM/DD HH:mm".
     * <p></p>
     * Passing null will reset it to its default.
     *
     * @param pattern date time format pattern
     * @return self
     */
    public NotificationI18N setDateTimeFormatPattern(String pattern) {
        this.dateTimeFormatPattern = pattern;
        return this;
    }
}
