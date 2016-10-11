package com.pint.BusinessLogic.Validators;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Services.UserService;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Presentation.Controllers.Session;
import com.pint.Presentation.Controllers.UserProvider;

/**
 * Created by Dionny on 11/28/2015.
 */
public class InputDonorValidator extends Validator {

    private User user;
    private UserService service;
    private String email;
    private BloodDrive drive;

    public InputDonorValidator(User user, UserService service, String email, BloodDrive drive) {
        this.user = user;
        this.service = service;
        this.email = email;
        this.drive = drive;
    }

    @Override
    protected boolean validObject() {
        if (!drive.getEmployees().contains(new Employee(user.getId()))) {
            error = "Invalid blood drive.";
            return false;
        }

        error = "Donor with provided email address does not exist.";

        try {
            User user = service.getUserByEmail(email);
            if (user != null && user.isDonor()) {
                error = null;
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    protected boolean validPermissions() {
        try {
            return user.isNurse();
        } catch (Exception e) {
            return false;
        }
    }
}
