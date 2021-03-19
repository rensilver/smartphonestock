package com.rensilver.smartphone_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SmartphoneStockExceededException extends Exception {

    public SmartphoneStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Smartphones with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
