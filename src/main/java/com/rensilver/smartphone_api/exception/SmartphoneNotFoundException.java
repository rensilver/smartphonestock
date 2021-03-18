package com.rensilver.smartphone_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SmartphoneNotFoundException extends Exception {

    public SmartphoneNotFoundException(Long smartphoneId) {
        super(String.format("Smartphone with name %s not found in the system.", smartphoneId));
    }

    public SmartphoneNotFoundException(String smartphoneName) {
        super(String.format("Smartphone with name %s not found in the system.", smartphoneName));
    }
}
