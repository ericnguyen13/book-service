package com.nguyen.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
public class HttpRequestLog {
    private String name;
    private String method;
    private String url;
    private String queryString;
    private List<Cookie> cookies;
    private Map<String, String> headers;
    private String serverAddress;
    private String body;

    public HttpRequestLog(ContentCachingRequestWrapper rawRequest) {
        this.name = "HTTP-Request";
        this.method = rawRequest.getMethod();
        this.url = rawRequest.getRequestURL().toString();
        this.queryString = rawRequest.getQueryString();
        Cookie [] cookies = rawRequest.getCookies();
        this.cookies = cookies != null ? Arrays.asList(cookies) : new ArrayList<>();
        this.headers = new HashMap<>();
        Collections.list(rawRequest.getHeaderNames()).forEach(name -> this.headers.put(name, rawRequest.getHeader(name)));
        this.serverAddress = rawRequest.getLocalAddr();
        this.body = new String(rawRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
