package com.github.balchua.workflowdemo.argumenthandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Data
@Slf4j
public class UiProxy {
    HttpEntity<String> httpEntity;
    HttpMethod httpMethod;
    MultiValueMap parameters;

    public ResponseEntity<String> uri(String baseUri, String path) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUri).path(path)
                .queryParams(parameters)
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
    }
}
