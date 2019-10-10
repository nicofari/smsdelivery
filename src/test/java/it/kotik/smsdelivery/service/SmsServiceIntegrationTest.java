package it.kotik.smsdelivery.service;

import it.kotik.smsdelivery.domain.Sms;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsServiceIntegrationTest {

    @Autowired
    SmsService smsService;

    @Test
    public void should_paginate() {
        List<Sms> smsList = buildList(10);

        List<Sms> actual = smsService.findAll(8, 2);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getBody().equals("9"));
    }

    private List<Sms> buildList(int size) {
        List<Sms> ret = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            ret.add(createSms(i));
        }
        return ret;
    }

    private Sms createSms(Integer num) {
        return smsService.save(new Sms("userId", "+00116851750", "+00217561791", num.toString()));
    }


}
