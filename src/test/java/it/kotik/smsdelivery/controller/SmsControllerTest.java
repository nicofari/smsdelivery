package it.kotik.smsdelivery.controller;

import it.kotik.smsdelivery.domain.Sms;
import it.kotik.smsdelivery.service.SmsService;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.StringUtil.join;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SmsController.class)
public class SmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SmsService smsService;

    @Test
    public void should_check_required_fields() throws Exception {
        RequestBuilder builder = post("/public/api/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"\",",
                        "\"destNumber\": \"\", ",
                        "\"body\": \"\"",
                        "}"));

        mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Is.is("Invalid body")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destNumber", Is.is("Invalid phone number")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sourceNumber", Is.is("Invalid phone number")));
    }

    @Test
    public void should_check_numbers() throws Exception {
        RequestBuilder builder = post("/public/api/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"a123\",",
                        "\"destNumber\": \"+22aaa\", ",
                        "\"body\": \"i'm an sms text\"",
                        "}"));

        mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sourceNumber", Is.is("Invalid phone number")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destNumber", Is.is("Invalid phone number")));
    }

    @Test
    public void should_check_body() throws Exception {
        RequestBuilder builder = post("/public/api/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"+00117561791\",",
                        "\"destNumber\": \"+00216851750\", ",
                        "\"body\": \"test ®\"",
                        "}"));

        mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Is.is("Invalid body")));
    }

    @Test
    public void should_create_sms_as_expected() throws Exception {
        Sms sms = mock(Sms.class);
        given(sms.getIdAsString()).willReturn("00000000-0000-0000-0000-000000000010");
        given(smsService.save(ArgumentMatchers.any(Sms.class))).willReturn(sms);

        RequestBuilder builder = post("/public/api/v1/sms/create")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(join("{",
                        "\"userId\": \"userId\",",
                        "\"sourceNumber\": \"+00117561791\",",
                        "\"destNumber\": \"+00216851750\", ",
                        "\"body\": \"test Ω\"",
                        "}"));

        mockMvc.perform(builder)
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.href", Is.is("/public/api/v1/sms/00000000-0000-0000-0000-000000000010")));
    }
}
