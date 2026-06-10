package org.membership.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    public final String id;
    public final String name;
    public final String cohort; 
    public final List<Purchase> purchases = new ArrayList<>();

    public User(String id, String name, String cohort) {
        this.id = id; this.name = name; this.cohort = cohort;
    }

    public synchronized void recordPurchase(Purchase p) {
        purchases.add(p);
    }

    @Override public String toString() {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"cohort\":\"%s\"}", id, name, cohort);
    }
}