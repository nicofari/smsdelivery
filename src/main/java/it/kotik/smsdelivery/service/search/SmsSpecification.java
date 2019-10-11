package it.kotik.smsdelivery.service.search;

import it.kotik.smsdelivery.domain.Sms;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SmsSpecification implements Specification<Sms> {
    private final SearchCriteria searchCriteria;

    public SmsSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Sms> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (searchCriteria.getOperator().equalsIgnoreCase(">")) {
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()),
                    cb.isNotNull(root.get(searchCriteria.getKey())));
        } else if (searchCriteria.getOperator().equalsIgnoreCase("<")) {
            return cb.and(
                    cb.lessThanOrEqualTo(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString()),
                    cb.isNotNull(root.get(searchCriteria.getKey())));
        } else if (searchCriteria.getOperator().equalsIgnoreCase(":")) {
            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return cb.and(
                        cb.like(root.<String>get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%"),
                        cb.isNotNull(root.get(searchCriteria.getKey())));
            } else {
                return cb.and(
                        cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue()),
                        cb.isNotNull(root.get(searchCriteria.getKey())));
            }
        }
        return null;
    }
}
