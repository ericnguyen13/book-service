package com.nguyen.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactory {
    public Logger getLogger(Class<?> klass) {
        return LogManager.getLogger(klass);
    }
}
