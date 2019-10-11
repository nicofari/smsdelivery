package it.kotik.smsdelivery.service.search;

import it.kotik.smsdelivery.domain.Sms;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SmsSearchCriteriaBuilder {
    private final List<SearchCriteria> criteriaList;

    public SmsSearchCriteriaBuilder() {
        criteriaList = new ArrayList<>();
    }

    public SmsSearchCriteriaBuilder with(String key, String operator, Object value) {
        criteriaList.add(new SearchCriteria(key, operator, value));
        return this;
    }

    public Specification<Sms> build() {
        if (criteriaList.size() == 0) {
            return null;
        }
        List<Specification> specs = criteriaList.stream()
                .map(SmsSpecification::new)
                .collect(Collectors.toList());

        Specification ret = specs.get(0);
        for (int i = 1; i < criteriaList.size(); i++) {
            ret = Specification.where(ret).and(specs.get(i));
        }
        return ret;
    }
}
