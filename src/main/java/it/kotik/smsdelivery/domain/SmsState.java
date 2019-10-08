package it.kotik.smsdelivery.domain;

public enum SmsState {
    VERIFICATION_PENDING,
    ACCEPTED,
    REFUSED,
    CONFIRMED,
    SENT,
    DELIVERED,
    BOUNCED
}
