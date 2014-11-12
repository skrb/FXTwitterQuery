package net.javainthebox.twitter.fx;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import twitter4j.User;

public class StatusView {
    private final ObjectProperty<Date> date = new SimpleObjectProperty<>();
    private final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private final StringProperty text = new SimpleStringProperty();
    
    public StatusView (Date date, User user, String text) {
        this.date.set(date);
        this.user.set(user);
        this.text.set(text);
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date value) {
        date.set(value);
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public User getUser() {
        return user.get();
    }

    public void setUser(User value) {
        user.set(value);
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String value) {
        text.set(value);
    }

    public StringProperty textProperty() {
        return text;
    }
}
