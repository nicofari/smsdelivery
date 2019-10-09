package it.kotik.smsdelivery.repository;

import it.kotik.smsdelivery.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SmsRepository extends JpaRepository<Sms, UUID> {
    Sms save(Sms sms);
    Optional<Sms> findById(UUID id);
    void delete(Sms sms);
}
