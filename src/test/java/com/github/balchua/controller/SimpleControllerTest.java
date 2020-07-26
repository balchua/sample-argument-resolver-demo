package com.github.balchua.controller;

import com.github.balchua.LocalTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {LocalTestConfiguration.class},
        properties = {
                "security.basic.enabled=false"
        })
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles({"test"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})

public class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RestTemplate restTemplate;

    private ResponseEntity<String> responseEntity;
    private HttpHeaders headers;

    @BeforeEach
    private void initMocks() {
        headers = new HttpHeaders();
        headers.add("authorization", "Basic: somebase64");
        responseEntity = ResponseEntity.created(buildUri())
                .headers(headers)
                .body("Hello World");

        when(restTemplate.exchange(ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);

    }


    private URI buildUri() {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", "bal");
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost").path("/somepath")
                .queryParams(parameters)
                .encode()
                .build()
                .toUri();
        return uri;

    }

    @Test
    @DisplayName("Should pass through the UiProxy, returning the mock response")
    void shouldPassThroughTheUiProxy() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/proxied")
                .param("name", "bal")
                .headers(headers))
                .andExpect(status().is(201))
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("Should pass the same headers and request parameters")
    void shouldPassTheSameHeadersAndParameters() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/proxied")
                .param("name", "bal")
                .headers(headers))
                .andExpect(status().is(201))
                .andReturn();

        ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
        ArgumentCaptor<HttpMethod> httpMethodArgumentCaptor = ArgumentCaptor.forClass(HttpMethod.class);
        ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        verify(restTemplate).exchange(uriArgumentCaptor.capture()
                , httpMethodArgumentCaptor.capture()
                , httpEntityArgumentCaptor.capture()
                , ArgumentMatchers.<Class<String>>any());

        URI uri = uriArgumentCaptor.getValue();
        assertThat(uri.toString()).isEqualTo("http://localhost:8080/simple?name=bal");

        HttpMethod method = httpMethodArgumentCaptor.getValue();
        assertThat(method).isEqualTo(HttpMethod.GET);

        HttpEntity entity = httpEntityArgumentCaptor.getValue();
        assertThat(entity.getHeaders()).isEqualTo(headers);

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Hello World");
    }
}
