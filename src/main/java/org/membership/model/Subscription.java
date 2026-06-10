package org.membership.model;

import java.time.Instant;

public class Subscription {
    public final String id;
    public final String userId;
    public final String planId;
    public MemberShipTier tier;
    public final Instant startAt;
    public Instant expiresAt;
    public Status status;

    public enum Status { ACTIVE, CANCELLED, EXPIRED }

    public Subscription(String id, String userId, String planId, MemberShipTier tier, Instant startAt, Instant expiresAt) {
        this.id = id; this.userId = userId; this.planId = planId; this.tier = tier; this.startAt = startAt; this.expiresAt = expiresAt; this.status = Status.ACTIVE;
    }

}
