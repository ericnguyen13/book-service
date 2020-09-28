package com.nguyen.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ApiException extends Throwable {
    private HttpStatus status;
    private String message;
}
