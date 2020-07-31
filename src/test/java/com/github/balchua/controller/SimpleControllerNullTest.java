package com.github.balchua.controller;

import com.github.balchua.LocalTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

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
