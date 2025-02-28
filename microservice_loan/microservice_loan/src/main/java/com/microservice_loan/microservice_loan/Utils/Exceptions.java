package com.microservice_loan.microservice_loan.Utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class Exceptions {
    public static Map<String, String> getExceptionsErrors(BindingResult result) {
        Map<String, String> mistake = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            mistake.put(error.getField(), error.getDefaultMessage());
        }
        return mistake;
    }
}
