package com.ssafy.mugit.global.exception;

import com.ssafy.mugit.global.exception.error.FlowApiError;
import lombok.Getter;

@Getter
public class FlowApiException extends RuntimeException {
    private final FlowApiError flowApiError;
    private final String message;

    public FlowApiException(FlowApiError flowApiError) {
        this.flowApiError = flowApiError;
        this.message = flowApiError.getMessage();
    }
}
