/*
 * Copyright (c) 2020 Artem Merets
 */

package ru.otus.merets.atm.exception;

public class IncorrectAmountException extends ATMException {
    public IncorrectAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
