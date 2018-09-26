package com.matches.jobMatcher.Exception;

import java.util.ArrayList;
import java.util.List;

public class JobMatcherException extends Exception {

    private List<String> errors;
    private String message;
    private String code;

    public JobMatcherException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public List<String> getErrors() {
        errors = new ArrayList<>();
        errors.add(message);
        return errors;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
