package com.ao.depress.stocknotify.model.observer;

public interface Subject {
    void subscribe(Observer o);
    void unsubscribe(Observer o);
    boolean isSubscribed(Observer o);
}
