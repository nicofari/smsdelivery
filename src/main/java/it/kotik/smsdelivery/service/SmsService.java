package it.kotik.smsdelivery.service;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.repository.SmsRepository;
import it.kotik.smsdelivery.service.paging.OffsetBasedPageRequest;
import it.kotik.smsdelivery.service.search.SmsSearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SmsService {

    @Autowired
    SmsRepository smsRepository;

    public Sms save(Sms sms) {
        return smsRepository.save(sms);
    }

    public Optional<Sms> findById(UUID id) {
        return smsRepository.findById(id);
    }

    public void delete(Sms sms) {
        smsRepository.delete(sms);
    }

    public List<Sms> findAll(long offset, int limit, String[] sortBy, String filter) {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit, buildSort(sortBy));
        Specification<Sms> smsSpecification = buildSmsSpecification(filter);
        return smsRepository.findAll(smsSpecification, pageRequest).getContent();
    }

    private Specification<Sms> buildSmsSpecification(String filter) {
        if (filter == null || filter.isEmpty()) {
            return null;
        }
        SmsSearchCriteriaBuilder smsSearchCriteriaBuilder = new SmsSearchCriteriaBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([\\w|-]+?),");
        Matcher matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            smsSearchCriteriaBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        return smsSearchCriteriaBuilder.build();
    }

    private Sort buildSort(String[] sortBy) {
        return Sort.by(
                Arrays.stream(sortBy)
                        .map(sort -> sort.split(";", 2))
                        .map(array ->
                                new Sort.Order(replaceOrderStringThroughDirection(array[1]), array[0]).ignoreCase()
                        ).collect(Collectors.toList()));
    }

    private Sort.Direction replaceOrderStringThroughDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("DESC")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }
}
