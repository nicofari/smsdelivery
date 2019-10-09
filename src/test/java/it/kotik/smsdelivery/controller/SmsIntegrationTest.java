package it.kotik.smsdelivery.controller;

import com.jayway.jsonpath.JsonPath;
import it.kotik.smsdelivery.Main;
import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.repository.SmsRepository;
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

import static org.mockito.internal.util.StringUtil.join;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SmsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SmsRepository smsRepository;

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
        return post("/v1/sms/create")
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

        mockMvc.perform(put("/v1/sms/" + sms.getIdAsString() + "/send"))
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

        mockMvc.perform(delete("/v1/sms/" + sms.getIdAsString()))
                .andExpect(status().isOk());
    }

    @Test
    public void should_fail_delete_sms() throws Exception {
        Sms sms = createSms();
        confirmSms(sms);

        mockMvc.perform(delete("/v1/sms/" + sms.getIdAsString()))
                .andExpect(status().isUnprocessableEntity());
    }

}