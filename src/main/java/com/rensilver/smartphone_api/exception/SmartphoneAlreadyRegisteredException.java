package com.rensilver.smartphone_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SmartphoneAlreadyRegisteredException extends Exception {

    public SmartphoneAlreadyRegisteredException(String smartphoneName) {
        super(String.format("Smartphone with name %s already registered in the system.", smartphoneName));
    }
}
