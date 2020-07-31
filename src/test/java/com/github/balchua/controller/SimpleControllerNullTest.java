package com.github.balchua.controller;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SimpleControllerNullTest {

    @Autowired
    private MockMvc mockMvc;

    private RestTemplate restTemplate;

    private ResponseEntity<String> responseEntity;
    private HttpHeaders headers;

    @BeforeEach
    private void initMocks(TestInfo info) {
        headers = new HttpHeaders();
        headers.add("authorization", "Basic: somebase64");
    }

    @Test
    @DisplayName("Should throw NullPointerException when restTemplate is null")
    void shouldNotAllowNullRestTemplate() throws Exception {
        Assertions.assertThrows(NullPointerException.class, () -> {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/proxied")
                    .param("name", "bal")
                    .headers(headers))
                    .andExpect(status().is(201))
                    .andReturn();
        });
    }
}
