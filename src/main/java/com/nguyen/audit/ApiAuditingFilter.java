package com.nguyen.audit;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiAuditingFilter extends OncePerRequestFilter {
    private final Logger logger;

    public ApiAuditingFilter(LoggerFactory loggerFactory) {
        this.logger = loggerFactory.getLogger(ApiAuditingFilter.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // auditing the api request and response.
        if (request.getRequestURI().startsWith("/api")) {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            String requestJson = new HttpRequestLog(requestWrapper).toString();
            try {
                filterChain.doFilter(requestWrapper, responseWrapper);
                logger.info(requestJson);
                logger.info(new HttpResponseLog(responseWrapper).toString());
                responseWrapper.copyBodyToResponse();
            } catch (Exception ex){
                logger.info(requestJson);
                logger.error(ex);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
