package com.github.balchua.argumenthandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor
@Slf4j
public class UiProxy {
    @Getter
    private HttpEntity<String> httpEntity;
    @Getter
    private HttpMethod httpMethod;
    @Getter
    private MultiValueMap parameters;

    public ResponseEntity<String> uri(@NonNull RestTemplate restTemplate, String baseUri, String path) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUri).path(path)
                .queryParams(parameters)
                .encode()
                .build()
                .toUri();
        return restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
    }
}
