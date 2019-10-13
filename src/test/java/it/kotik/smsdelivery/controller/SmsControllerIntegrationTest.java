package it.kotik.smsdelivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import it.kotik.smsdelivery.Main;
import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.domain.dto.SmsPageDto;
import it.kotik.smsdelivery.repository.SmsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.internal.util.StringUtil.join;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SmsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SmsRepository smsRepository;

    @Before
    public void before() {
        smsRepository.deleteAll();
    }

    @Test
    public void should_create_and_get_sms() throws Exception {
        RequestBuilder postRequest = getSmsCreateRequest();

        String href = postCreateAndGetHref(postRequest);

        MvcResult getResult = mockMvc.perform(get(href))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.userId").value("userId"))
                .andExpect(jsonPath("$.body").value("test Ω"))
                .andExpect(jsonPath("$.state").value("ACCEPTED"))
                .andReturn();
    }

    private String postCreateAndGetHref(RequestBuilder postRequest) throws Exception {
        MvcResult postResult = mockMvc.perform(postRequest)
                .andExpect(status().is(200)).andReturn();
        String postResponse = postResult.getResponse().getContentAsString();
        return JsonPath.parse(postResponse).read("href");
    }

    private RequestBuilder getSmsCreateRequest() {
        return post("/public/api/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"+00117561791\",",
                        "\"destNumber\": \"+00216851750\", ",
                        "\"body\": \"test Ω\"",
                        "}"));
    }

    @Test
    public void should_mark_sent_sms_as_confirmed() throws Exception {
        RequestBuilder postRequest = getSmsCreateRequest();

        String href = postCreateAndGetHref(postRequest);

        mockMvc.perform(put(href + "/send"))
                .andExpect(status().isOk());

        mockMvc.perform(get(href))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.state").value("CONFIRMED"));
    }

    @Test
    public void send_sms_should_fail() throws Exception {
        Sms sms = createSms();
        confirmSms(sms);

        mockMvc.perform(put("/public/api/v1/sms/" + sms.getIdAsString() + "/send"))
                .andExpect(status().isUnprocessableEntity());
    }

    private void confirmSms(Sms sms) {
        sms.confirm();
        smsRepository.save(sms);
    }

    private Sms createSms() {
        return smsRepository.save(new Sms("userId", "+00116851750", "+00217561791", "some text"));
    }

    @Test
    public void should_delete_sms() throws Exception {
        Sms sms = createSms();

        mockMvc.perform(delete("/public/api/v1/sms/" + sms.getIdAsString()))
                .andExpect(status().isOk());
    }

    @Test
    public void should_fail_delete_sms() throws Exception {
        Sms sms = createSms();
        confirmSms(sms);

        mockMvc.perform(delete("/public/api/v1/sms/" + sms.getIdAsString()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_return_paginated_and_sorted_results() throws Exception {
        createBatch(10);

        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/sms?limit=2&offset=8&sort=receptionDate;DESC"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SmsPageDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), SmsPageDto.class);
        assertThat(actual.getContent().size()).isEqualTo(2);

        mvcResult = mockMvc.perform(get("/public/api/v1/sms?limit=18&offset=0"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), SmsPageDto.class);
        assertThat(actual.getContent().size()).isEqualTo(10);
    }

    private List<Sms> createBatch(int size) {
        List<Sms> ret = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ret.add(createSms());
        }
        return ret;
    }

    @Test
    public void should_filter_results() throws Exception {
        List<Sms> db = createBatch(10);
        setSentDate(db.get(2), "3000-01-08T22:00");

        MvcResult mvcResult = mockMvc.perform(get("/public/api/v1/sms?filter=sentDate>3000-01-01,sentDate<3000-01-10"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SmsPageDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), SmsPageDto.class);

        assertThat(actual.getContent().size()).isEqualTo(1);
    }

    private void setSentDate(Sms sms, String sentDate) {
        sms.setSentDate(sentDate);
        smsRepository.save(sms);
    }
}
