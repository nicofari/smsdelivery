package it.kotik.smsdelivery.repository;

import it.kotik.smsdelivery.domain.Sms;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends CrudRepository<Sms, Long> {
}
