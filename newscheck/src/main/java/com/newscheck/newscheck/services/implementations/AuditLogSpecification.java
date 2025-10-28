package com.newscheck.newscheck.services.implementations;

import com.newscheck.newscheck.models.AuditLogModel;
import com.newscheck.newscheck.models.requests.AuditLogFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification implements Specification<AuditLogModel> {

    private final AuditLogFilterDTO filter;

    public AuditLogSpecification(AuditLogFilterDTO filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(jakarta.persistence.criteria.Root<AuditLogModel> root,
                                 jakarta.persistence.criteria.CriteriaQuery<?> query,
                                 jakarta.persistence.criteria.CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("userId"), filter.getUserId()));
        }

        if (filter.getAction() != null) {
            predicates.add(cb.equal(root.get("action"), filter.getAction()));
        }

        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.getEndDate()));
        }

        if (filter.getIpAddress() != null && !filter.getIpAddress().isEmpty()) {
            predicates.add(cb.like(root.get("ipAddress"), "%" + filter.getIpAddress() + "%"));
        }

        if (filter.getSuccess() != null) {
            predicates.add(cb.equal(root.get("success"), filter.getSuccess()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}