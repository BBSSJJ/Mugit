package com.ssafy.mugit.global.exception;


import com.ssafy.mugit.global.exception.error.MugitoryError;
import lombok.Getter;

@Getter
public class MugitoryException extends Exception{
    private final MugitoryError mugitoryError;
    private final String message;

    public MugitoryException(final MugitoryError mugitoryError) {
        this.mugitoryError = mugitoryError;
        this.message = mugitoryError.getMessage();
    }
}
