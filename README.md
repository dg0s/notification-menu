# Notification Menu for Vaadin 14+

## Usage

Some examples of this component usage:

```java
static final List<NotificationItem> items = new ArrayList<NotificationItem>() {{
    add(new NotificationItem(NotificationType.INFO, "Movie Release", "John Wick 4 is coming soon!"));
    add(new NotificationItem(NotificationType.SUCCESS, "Movie Release", "The Matrix Resurrections is coming soon!"));
}};

final NotificationMenu notificationMenu = new NotificationMenu();
notificationMenu.setItems(items);
        
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
```

## Setting up for development:

Clone the project in GitHub

```
git clone git@github.com:dg0s/notification-menu.git
```

to install project, to your maven repository run

```mvn install```


## How to run the demo?

The Demo can be run going to the project `notification-menu-demo` and executing the maven goal:

```mvn spring-boot:run```

## How to deploy

```mvn clean install -Pproduction,directory```

# License & Author

Apache License 2