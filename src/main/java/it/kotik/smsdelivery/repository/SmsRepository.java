package it.kotik.smsdelivery.repository;

import it.kotik.smsdelivery.domain.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SmsRepository extends JpaRepository<Sms, UUID>, JpaSpecificationExecutor<Sms> {
    Sms save(Sms sms);
    Optional<Sms> findById(UUID id);
    void delete(Sms sms);
    Page<Sms> findAll(Specification<Sms> smsSpecification, Pageable pageable);
}
