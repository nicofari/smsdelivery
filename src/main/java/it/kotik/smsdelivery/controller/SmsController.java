package it.kotik.smsdelivery.controller;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.domain.dto.SmsHrefDto;
import it.kotik.smsdelivery.domain.dto.SmsPageDto;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/public/api")
public class SmsController {

    @Autowired
    SmsService smsService;

    @Transactional
    @PostMapping(path = "v1/sms/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SmsHrefDto> createSms(@Valid @RequestBody Sms smsDto) {
        Sms sms = smsService.save(smsDto);
        return ok().body(new SmsHrefDto(sms.getIdAsString()));
    }

    @GetMapping(path = "v1/sms/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
        Optional<Sms> optSms = smsService.findById(id);
        if (!optSms.isPresent()) {
            return notFound().build();
        }
        Sms sms = optSms.get();
        if (!sms.isAccepted()) {
            return unprocessableEntity().build();
        } else {
            sms.confirm();
            smsService.save(sms);
            return ok().build();
        }
    }

    @Transactional
    @DeleteMapping(path = "/v1/sms/{id}")
    public ResponseEntity<?> deleteSms(@PathVariable UUID id) {
        Optional<Sms> optSms = smsService.findById(id);
        if (!optSms.isPresent()) {
            return notFound().build();
        }
        Sms sms = optSms.get();
        if (!sms.isDeletable()) {
            return unprocessableEntity().build();
        } else {
            smsService.delete(sms);
            return ok().build();
        }
    }

    @GetMapping(path = "/v1/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SmsPageDto getAll(
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "limit", defaultValue = "25") int limit,
            @RequestParam(value = "sort", defaultValue = "receptionDate;ASC", required = false) String[] sortBy,
            @RequestParam(value = "filter", required = false) String filter) {
        return smsService.findAll(offset, limit, sortBy, filter);
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
