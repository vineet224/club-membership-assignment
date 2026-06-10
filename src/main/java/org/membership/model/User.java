package org.membership.model;

public class User {
    public final String id;
    public final String name;
    public final String cohort; // optional cohort tag // but this must not be here as user can belong to multiple cohort at a single time
    // why we don't have the membership tier here? because a user can have multiple subscriptions with different tiers over time

    public User(String id, String name, String cohort) {
        this.id = id; this.name = name; this.cohort = cohort;
    }

    @Override public String toString() {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"cohort\":\"%s\"}", id, name, cohort);
    }
}