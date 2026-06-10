package org.membership.controller;

import org.membership.dto.PurchaseRequest;
import org.membership.dto.SubscriptionRequest;
import org.membership.exception.ActiveSubscriptionException;
import org.membership.model.MembershipPlan;
import org.membership.model.MemberShipTier;
import org.membership.model.Subscription;
import org.membership.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collection;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HttpController {
    private final MembershipService service;

    public HttpController(MembershipService service) {
        this.service = service;
    }

    @GetMapping("plans")
    public ResponseEntity<Collection<MembershipPlan>> listPlans() {
        Collection<MembershipPlan> all = service.listPlans();
        return ResponseEntity.ok(all);
    }

    @PostMapping("subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionRequest body) {
        if (body == null || body.userId() == null || body.planId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        MemberShipTier tier;
        try {
            tier = MemberShipTier.valueOf(body.tier() == null ? "SILVER" : body.tier().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Optional<Subscription> sub = service.subscribe(body.userId(), body.planId(), tier);
            if (sub.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(sub.get());
        } catch (ActiveSubscriptionException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("purchase")
    public ResponseEntity<Void> recordPurchase(@RequestBody PurchaseRequest req) {
        if (req == null || req.userId() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        service.recordPurchaseAndEvaluate(req.userId(), req.amount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("subscription/user/{userId}")
    public ResponseEntity<List<Subscription>> getByUser(@PathVariable String userId) {
        List<Subscription> list = service.getByUser(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("subscription/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable String id) {
        Optional<Subscription> s = service.getSubscription(id);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("subscription/{id}/upgrade")
    public ResponseEntity<Subscription> upgrade(@PathVariable String id) {
        Optional<Subscription> r = service.upgrade(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("subscription/{id}/downgrade")
    public ResponseEntity<Subscription> downgrade(@PathVariable String id) {
        Optional<Subscription> r = service.downgrade(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("subscription/{id}/cancel")
    public ResponseEntity<Subscription> cancel(@PathVariable String id) {
        Optional<Subscription> r = service.cancel(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}