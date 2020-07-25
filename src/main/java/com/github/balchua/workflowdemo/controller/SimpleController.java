package com.github.balchua.workflowdemo.controller;

import com.github.balchua.workflowdemo.argumenthandler.UiProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
public class SimpleController {

    @GetMapping("/proxied")
    public ResponseEntity<String> proxied(UiProxy proxy, Principal principal) {
        log.debug(principal.getName());
        return proxy.uri("http://localhost:8080", "/simple");
    }

    /*
    This is just a simulation.  Assume this is hosted in another remote service.
     */
    @GetMapping("/simple")
    public String simple(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("name") String name) {
        log.info("Parameter Name {}", name);
        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value));
        });
        String html = String.format("<html><body><h1>Hi, im %s</h1></body></html>", name);
        return html;
    }
}
