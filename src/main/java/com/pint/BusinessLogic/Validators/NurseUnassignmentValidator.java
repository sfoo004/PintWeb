package com.pint.BusinessLogic.Validators;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.Data.Models.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dionny on 11/27/2015.
 */
public class NurseUnassignmentValidator extends Validator {

    private final User user;
    private Employee coordinator;
    private ArrayList<Employee> validatedNurses;
    private List<Long> nurses;

    public NurseUnassignmentValidator(
            User user, Employee coordinator,
            List<Long> nurses) {
        this.user = user;
        this.coordinator = coordinator;
        this.nurses = nurses;
        this.validatedNurses = new ArrayList<>();
    }

    public ArrayList<Employee> getValidatedObjects() {
        return validatedNurses;
    }

    @Override
    protected boolean validObject() {
        for (Long nurse :
                nurses) {
            validatedNurses.add(new Employee(nurse));
        }
        return true;
    }

    @Override
    protected boolean validPermissions() {
        // Is this coordinator authorized for this request?
        return user.hasRole(UserRole.COORDINATOR)
                && coordinator.getUserId() == user.getId();
    }
}
