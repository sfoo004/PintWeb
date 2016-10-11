package com.pint.BusinessLogic.Validators;

/**
 * Created by Dionny on 11/26/2015.
 */
public abstract class Validator {
    protected String error;

    public boolean Validate() {
        return validPermissions() && validObject();
    }

    protected abstract boolean validObject();
    protected abstract boolean validPermissions();

    public String getError() {
        return error;
    }
}
