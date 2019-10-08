package it.kotik.smsdelivery.controller;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.domain.SmsState;
import it.kotik.smsdelivery.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class SmsController {

    @Autowired
    SmsRepository smsRepository;

    @Transactional
    @PostMapping(
            path = "/v1/sms/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<ResponseDto> createSms(@Valid @RequestBody Sms sms) {
        Sms persistedSms = smsRepository.save(sms);
        return ok()
                .body(new ResponseDto("/v1/sms/" + persistedSms.getIdAsString(), null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @GetMapping(
            path = "v1/sms/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> getSms(@PathVariable UUID id) {
        Optional<Sms> sms = smsRepository.findById(id);
        if (sms.isPresent()) {
            return ok().body(sms);
        } else {
            return notFound().build();
        }
    }

    @PutMapping(
            path = "v1/sms/{id}/send",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> sendSms(@PathVariable UUID id) {
        Optional<Sms> sms = smsRepository.findById(id);
        if (!sms.isPresent()) {
            return notFound().build();
        }
        Sms actual = sms.get();
        if (actual.getState() != SmsState.ACCEPTED) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } else {
            actual.setState(SmsState.CONFIRMED);
            smsRepository.save(actual);
            return ResponseEntity.ok().build();
        }
    }
}
