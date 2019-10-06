package it.kotik.smsdelivery.controller;

import it.kotik.smsdelivery.repository.SmsRepository;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;

import static org.mockito.internal.util.StringUtil.join;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SmsController.class)
public class SmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SmsRepository smsRepository;

    @Test
    public void should_reject_invalid_input() throws Exception {
        RequestBuilder builder = post("/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"1\",",
                        "\"destNumber\": \"2\", ",
                        "\"body\": \"\"",
                        "}"));

        mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Is.is("Body is required")));
    }

}
