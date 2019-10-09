package it.kotik.smsdelivery.domain;

import java.util.EnumSet;

public enum SmsState {
    VERIFICATION_PENDING,
    ACCEPTED,
    REFUSED,
    CONFIRMED,
    SENT,
    DELIVERED,
    BOUNCED;

    public static EnumSet<SmsState> deletableStates() {
        return EnumSet.of(VERIFICATION_PENDING, ACCEPTED, REFUSED);
    }
}
