package com.pint.BusinessLogic.Validators;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.Data.Models.Employee;
import com.pint.Presentation.Controllers.Session;

/**
 * Created by Dionny on 11/27/2015.
 */
public class CreateEmployeeValidator extends Validator {
    private final Iterable<User> users;
    private final User user;
    private final Employee employee;
    private UserRole role;

    public CreateEmployeeValidator(Iterable<User> users, User user, Employee employee, UserRole role) {
        this.users = users;
        this.user = user;
        this.employee = employee;
        this.role = role;
    }

    @Override
    protected boolean validObject() {
        if (user.getUsername().length() < 4 || user.getUsername().length() > 30) {
            error = "The username must be between 4-30 characters long.";
            return false;
        }

        if (employee.getPassword().length() < 4 || employee.getPassword().length() > 100) {
            error = "The password must be between 4-100 characters long.";
            return false;
        }

        if (employee.getFirstName().length() < 1 || employee.getFirstName().length() > 50) {
            error = "The first name must be between 1-50 characters long.";
            return false;
        }

        if (employee.getLastName().length() < 1 || employee.getLastName().length() > 50) {
            error = "The last name must be between 1-50 characters long.";
            return false;
        }

        if (employee.getPhoneNumber().length() < 1 || employee.getPhoneNumber().length() > 50) {
            error = "The phone number must be between 1-15 characters long.";
            return false;
        }

        if(role == null){
            error = "Invalid role.";
            return false;
        }

        // The user name must not be taken.
        for (User u :
                users) {
            if (u.getUsername().equals(user.getUsername())) {
                error = "The username is taken. Please select another username.";
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean validPermissions() {
        return true;
    }
}
