package org.membership.controller;

import org.membership.dto.SubscriptionRequest;
import org.membership.model.MembershipPlan;
import org.membership.model.MemberShipTier;
import org.membership.model.Subscription;
import org.membership.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class HttpController {
    private final MembershipService service;

    public HttpController(MembershipService service) {
        this.service = service;
    }

    @GetMapping("plans")
    public ResponseEntity<java.util.Collection<MembershipPlan>> listPlans() {
        var all = service.listPlans();
        return ResponseEntity.ok(all);
    }

    @PostMapping("subscribe")
    public ResponseEntity<Subscription> subscribe(@RequestBody SubscriptionRequest body) {
        if (body == null || body.userId() == null || body.planId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        MemberShipTier tier;
        try {
            tier = MemberShipTier.valueOf(body.tier() == null ? "SILVER" : body.tier().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var sub = service.subscribe(body.userId(), body.planId(), tier);
        if (sub.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(sub.get());
    }

    @GetMapping("subscription/user/{userId}")
    public ResponseEntity<List<Subscription>> getByUser(@PathVariable String userId) {
        var list = service.getByUser(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("subscription/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable String id) {
        var s = service.getSubscription(id);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("subscription/{id}/upgrade")
    public ResponseEntity<Subscription> upgrade(@PathVariable String id) {
        var r = service.upgrade(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("subscription/{id}/downgrade")
    public ResponseEntity<Subscription> downgrade(@PathVariable String id) {
        var r = service.downgrade(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("subscription/{id}/cancel")
    public ResponseEntity<Subscription> cancel(@PathVariable String id) {
        var r = service.cancel(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}