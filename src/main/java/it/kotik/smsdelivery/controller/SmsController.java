package it.kotik.smsdelivery.controller;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class SmsController {

    @Autowired
    SmsService smsService;

    @Transactional
    @PostMapping(path = "/v1/sms/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResponseDto> createSms(@Valid @RequestBody Sms sms) {
        Sms persistedSms = smsService.save(sms);
        return ok().body(new ResponseDto(persistedSms.getIdAsString()));
    }

    @GetMapping(path = "/v1/sms/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getSms(@PathVariable UUID id) {
        Optional<Sms> sms = smsService.findById(id);
        if (sms.isPresent()) {
            return ok().body(sms);
        } else {
            return notFound().build();
        }
    }

    @Transactional
    @PutMapping(path = "/v1/sms/{id}/send")
    public ResponseEntity<String> sendSms(@PathVariable UUID id) {
        Optional<Sms> sms = smsService.findById(id);
        if (!sms.isPresent()) {
            return notFound().build();
        }
        Sms actual = sms.get();
        if (!actual.isAccepted()) {
            return unprocessableEntity().build();
        } else {
            actual.confirm();
            smsService.save(actual);
            return ok().build();
        }
    }

    @Transactional
    @DeleteMapping(path = "/v1/sms/{id}")
    public ResponseEntity<?> deleteSms(@PathVariable UUID id) {
        Optional<Sms> sms = smsService.findById(id);
        if (!sms.isPresent()) {
            return notFound().build();
        }
        Sms actual = sms.get();
        if (!actual.isDeletable()) {
            return unprocessableEntity().build();
        } else {
            smsService.delete(actual);
            return ok().build();
        }
    }

    @GetMapping(path = "/v1/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Sms> getAll(
            @RequestParam(value = "offset") long offset,
            @RequestParam(value = "limit") int limit,
            @RequestParam(value = "sort", defaultValue = "receptionDate;ASC", required = false) String[] sortBy) {
        return smsService.findAll(offset, limit, sortBy);
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
}
