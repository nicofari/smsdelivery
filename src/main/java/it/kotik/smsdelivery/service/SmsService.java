package it.kotik.smsdelivery.service;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Sms> findAll(long offset, int limit) {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(offset, limit, new Sort(Sort.DEFAULT_DIRECTION, "body"));
        return smsRepository.findAll(pageRequest).getContent();
    }
}
