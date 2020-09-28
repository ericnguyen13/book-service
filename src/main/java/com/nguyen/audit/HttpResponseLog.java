package com.nguyen.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpResponseLog {
    private String name;
    private int httpStatus;
    private Map<String, String> headers;
    private String body;

    public HttpResponseLog(ContentCachingResponseWrapper rawResponse) {
        name = "HTTP-Response";
        httpStatus = rawResponse.getStatus();
        this.headers = new HashMap<>();
        rawResponse.getHeaderNames().forEach(name -> this.headers.put(name, rawResponse.getHeader(name)));
        this.body = new String(rawResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
