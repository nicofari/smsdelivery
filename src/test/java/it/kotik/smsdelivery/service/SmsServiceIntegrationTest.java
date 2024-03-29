package it.kotik.smsdelivery.service;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.domain.dto.SmsPageDto;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsServiceIntegrationTest {

    @Autowired
    SmsService smsService;

    @Test
    public void should_paginate_and_sort() {
        List<Sms> smsList = buildSmsList(10, LocalDateTime.of(3000, 1, 1, 22, 0));

        SmsPageDto actual = smsService.findAll(8, 2, new String[]{"sentDate;DESC"}, null);

        assertThat(actual.getContent().size()).isEqualTo(2);
        assertThat(actual.getContent().get(0).getBody()).isEqualTo("2");
        assertThat(actual.getContent().get(1).getSentDate()).isEqualTo("3000-01-01T22:00");
    }

    private List<Sms> buildSmsList(int size, LocalDateTime startDate) {
        List<Sms> ret = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            ret.add(createSms(i, startDate.plusDays(i - 1).format(Sms.DATE_TIME_FORMATTER)));
        }
        return ret;
    }

    private Sms createSms(Integer num, String sentDate) {
        Sms sms = new Sms("userId", "+00116851750", "+00217561791", num.toString());
        sms.setSentDate(sentDate);
        return smsService.save(sms);
    }


}
