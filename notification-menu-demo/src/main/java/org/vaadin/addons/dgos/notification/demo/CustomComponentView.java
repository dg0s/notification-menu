package org.vaadin.addons.dgos.notification.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.dgos.notification.*;

import java.util.ArrayList;
import java.util.List;

@Route(value = "")
public class CustomComponentView extends VerticalLayout {

    static final List<NotificationItem> items = new ArrayList<NotificationItem>() {{
        add(new NotificationItem(NotificationType.INFO, "Movie Release", "John Wick 4 is coming soon!"));
        add(new NotificationItem(NotificationType.SUCCESS, "Movie Release", "The Matrix Resurrections is coming soon!"));
    }};
    private final VerticalLayout content;

    private final List<NotificationMenu> notificationMenus = new ArrayList<>();

    public CustomComponentView() {
        createToolbar();

        content = new VerticalLayout();
        content.setWidth("500px");
        add(content);
        setHorizontalComponentAlignment(Alignment.CENTER, content);

        createSample("Basic notification");

        NotificationMenu maxItemCount = createSample("Custom max item counter");
        maxItemCount.setTitle(null);
        maxItemCount.setMaxItemCount(5);
        maxItemCount.setMaxItemCountLabel("Max.");

        NotificationMenu customIcon = createSample("Custom icon");
        customIcon.setIcon(VaadinIcon.MAILBOX);

        // i18n German
        NotificationI18N notificationI18N = new NotificationI18N().setTitle("Benachrichtigungen")
                .setCaptionViewAll("Alle öffnen")
                .setCaptionMarkAllAsRead("Alle als gelesen markieren")
                .setDateTimeFormatPattern("dd.mm.yyyy HH:mm");

        NotificationMenu i18n = createSample("i18n");
        i18n.setI18n(notificationI18N);

        NotificationMenu customColors = createSample("Custom colors");
        customColors.addClassName("custom-colors");
    }

    private void createToolbar() {
        final HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.setWidthFull();
        actionsLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        final Button left = new Button("Orientation Left");
        left.addClickListener(event -> {
            notificationMenus.forEach(n -> n.setOrientation(Orientation.LEFT));
        });
        final Button right = new Button("Orientation Right");
        right.addClickListener(event -> {
            notificationMenus.forEach(n -> n.setOrientation(Orientation.RIGHT));
        });

        MenuBar addNotification = new MenuBar();
        addNotification.addThemeVariants(MenuBarVariant.LUMO_ICON);
        addNotification.addItem("Add Notification", event -> addNotification(NotificationType.INFO));
        MenuItem menuItem = addNotification.addItem(VaadinIcon.CHEVRON_DOWN.create());
        SubMenu subMenu = menuItem.getSubMenu();
        addSubMenuItem(subMenu, NotificationType.INFO);
        addSubMenuItem(subMenu, NotificationType.SUCCESS);
        addSubMenuItem(subMenu, NotificationType.WARNING);
        addSubMenuItem(subMenu, NotificationType.DANGER);
        addSubMenuItem(subMenu, NotificationType.UNKNOWN);

        Button clearAll = new Button("Clear all", event -> notificationMenus.forEach(NotificationMenu::setItems));

        actionsLayout.add(left, right, addNotification, new Button("Clear", event -> {
            items.clear();
            notificationMenus.forEach(NotificationMenu::clearAll);
        }), new Button("Mark all as read", event -> {
            notificationMenus.forEach(NotificationMenu::markAllAsRead);
        }), new Button("View all", event -> {
            notificationMenus.forEach(NotificationMenu::viewAll);
        }));


        Checkbox showHeader = new Checkbox("Show Header", true);
        Checkbox viewAllVisible = new Checkbox("View all visible", true);
        Checkbox markAllVisible = new Checkbox("Mark all visible", true);
        Checkbox triggerAnimation = new Checkbox("Enable Animation", false);
        HorizontalLayout secondActionLayout = new HorizontalLayout(showHeader, viewAllVisible, markAllVisible, triggerAnimation);
        secondActionLayout.setWidthFull();
        secondActionLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        showHeader.addValueChangeListener(event->notificationMenus.forEach(n->n.setHeaderVisible(event.getValue())));
        viewAllVisible.addValueChangeListener(event -> notificationMenus.forEach(n -> n.setViewAllButtonVisible(event.getValue())));
        markAllVisible.addValueChangeListener(event -> notificationMenus.forEach(n -> n.setMarkAllAsReadButtonVisible(event.getValue())));
        triggerAnimation.addValueChangeListener(event -> notificationMenus.forEach(n -> n.setAnimationEnable(event.getValue())));

        add(actionsLayout, secondActionLayout);
    }

    private NotificationMenu createSample(String sampleName) {
        final HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        final NotificationMenu notificationMenu = new NotificationMenu();
        notificationMenu.addItemClickEventListener(event -> {
            final NotificationItem selectedItem = event.getSelectedItem();
            Notification.show(selectedItem.getDescription(), 5000, Notification.Position.TOP_END).open();
            notificationMenu.markAsRead(selectedItem);
        });
        notificationMenu.addViewAllClickEventListener(event -> {
            Notification.show("Option 'View all' clicked!", 5000, Notification.Position.TOP_END).open();
        });

        notificationMenu.setAutoMarkAllAsRead(true);
        notificationMenu.addMarkAllAsReadClickEventListener(event -> {
            Notification.show("Option 'Mark all as read' clicked!", 5000, Notification.Position.TOP_END).open();
        });
        notificationMenu.setItems(items);
        Span span = new Span(sampleName);
        span.getStyle().set("font-weight", "bold");
        notificationLayout.add(span, notificationMenu);
        content.add(notificationLayout);

        notificationMenus.add(notificationMenu);

        return notificationMenu;
    }

    private MenuItem addSubMenuItem(SubMenu subMenu, NotificationType type) {
        return subMenu.addItem("Add \"" + type.name().toLowerCase() + "\"", event -> addNotification(type));
    }

    private void addNotification(NotificationType type) {
        items.add(new NotificationItem(type, "Movie Release", "Money Heist has been released today!"));
        notificationMenus.forEach(n -> n.setItems(items));
    }
}
