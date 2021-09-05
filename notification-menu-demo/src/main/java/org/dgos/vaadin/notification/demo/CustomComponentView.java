package org.dgos.vaadin.notification.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.dgos.vaadin.notification.NotificationItem;
import org.dgos.vaadin.notification.NotificationMenu;
import org.dgos.vaadin.notification.NotificationType;
import org.dgos.vaadin.notification.Orientation;

import java.util.ArrayList;
import java.util.List;

@Route(value = "")
@Theme(value = Lumo.class)
public class CustomComponentView extends VerticalLayout {

    static final List<NotificationItem> items = new ArrayList<NotificationItem>() {{
        add(new NotificationItem(NotificationType.INFO, "Movie Release", "John Wick 4 is coming soon!"));
        add(new NotificationItem(NotificationType.SUCCESS, "Movie Release", "The Matrix Resurrections is coming soon!"));
    }};

    public CustomComponentView() {
        setWidthFull();

        final HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.setWidthFull();
        notificationLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        final HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.setWidthFull();
        actionsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        add(notificationLayout, actionsLayout);

        final NotificationMenu notificationMenu = new NotificationMenu();
        notificationMenu.addItemClickEventListener(event -> {
            final NotificationItem selectedItem = event.getSelectedItem();
            Notification.show(selectedItem.getDescription(), 5000, Notification.Position.TOP_END).open();
            final int index = items.indexOf(selectedItem);
            items.get(index).markAsRead();
            notificationMenu.setItems(items);
        });
        notificationMenu.addViewAllClickEventListener(event -> {
            Notification.show("Option 'View all' clicked!", 5000, Notification.Position.TOP_END).open();
        });
        notificationMenu.addMarkAllAsReadClickEventListener(event -> {
            Notification.show("Option 'Mark all as read' clicked!", 5000, Notification.Position.TOP_END).open();
            items.forEach(NotificationItem::markAsRead);
            notificationMenu.setItems(items);
        });
        notificationMenu.setItems(items);
        notificationLayout.add(notificationMenu);

        final Button left = new Button("Orientation Left");
        left.addClickListener(event -> {
            notificationMenu.setOrientation(Orientation.LEFT);
        });
        final Button right = new Button("Orientation Right");
        right.addClickListener(event -> {
            notificationMenu.setOrientation(Orientation.RIGHT);
        });
        final Button add = new Button("Add Noticiation");
        add.addClickListener(event -> {
            items.add(new NotificationItem(NotificationType.SUCCESS, "Movie Release", "Money Heist has been released today!"));
            notificationMenu.setItems(items);
        });

        actionsLayout.add(left, right, add);
    }
}
