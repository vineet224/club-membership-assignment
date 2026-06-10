package org.membership.model;

import java.time.Instant;

public class Purchase {
    public final double amount;
    public final Instant at;

    public Purchase(double amount, Instant at) {
        this.amount = amount;
        this.at = at;
    }
}
