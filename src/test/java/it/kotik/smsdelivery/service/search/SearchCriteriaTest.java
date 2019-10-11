package it.kotik.smsdelivery.service.search;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SearchCriteriaTest {
    SearchCriteria target;

    @Test
    public void should_format_client_dates() {
        target = new SearchCriteria("sentDate", ">", "3000-01-01");

        LocalDateTime expected = LocalDateTime.of(3000, 1, 1, 0, 0);
        assertEquals(target.asDate(), expected);
    }
}
