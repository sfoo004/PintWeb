package com.pint.BusinessLogic.Validators;

/**
 * Created by Dionny on 11/27/2015.
 */
public class ValidationException extends Exception {
    public ValidationException(String error) {
        super(error);
    }
}
