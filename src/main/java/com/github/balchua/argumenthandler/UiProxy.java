package com.github.balchua.argumenthandler;

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
    private HttpEntity<String> httpEntity;
    private HttpMethod httpMethod;
    private MultiValueMap parameters;

    public ResponseEntity<String> uri(RestTemplate restTemplate,  String baseUri, String path) {
        if (restTemplate == null) {
            throw new IllegalArgumentException("restTemplate cannot be null.");
        }
        URI uri = UriComponentsBuilder
                .fromUriString(baseUri).path(path)
                .queryParams(parameters)
                .encode()
                .build()
                .toUri();
        return restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
    }
}
