package org.membership.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MembershipPlan {
    public final String id;
    public final String name;
    public final Duration duration;
    public final BigDecimal price;
    public final List<String> benefits;

    public enum Duration { MONTH, QUARTER, YEAR }

    public MembershipPlan(String id, String name, Duration duration, BigDecimal price, List<String> benefits) {
        this.id = id; this.name = name; this.duration = duration; this.price = price; this.benefits = List.copyOf(benefits);
    }
}
