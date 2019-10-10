package it.kotik.smsdelivery.service;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public List<Sms> findAll(long offset, int limit, String[] sortBy) {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit, buildSort(sortBy));
        return smsRepository.findAll(pageRequest).getContent();
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
