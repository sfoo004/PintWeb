package com.pint.BusinessLogic.Validators;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Services.BloodDriveService;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dionny on 11/26/2015.
 */
public class NurseAssignmentValidator extends Validator {
    private final User user;
    private final BloodDrive bd;
    private Employee coordinator;
    private List<Long> nurses;
    private List<Employee> validatedNurses;
    private List<Employee> allNurses;
    private List<BloodDrive> allBds;

    public NurseAssignmentValidator(
            User user, BloodDrive bd, Employee coordinator,
            List<Long> nurses,
            List<Employee> allNurses, List<BloodDrive> allBds) {
        this.user = user;
        this.bd = bd;
        this.coordinator = coordinator;
        this.nurses = nurses;
        this.allNurses = allNurses;
        this.allBds = allBds;
        this.validatedNurses = new ArrayList<>();
    }

    public List<Employee> getValidatedObjects() {
        return validatedNurses;
    }

    @Override
    protected boolean validObject() {
        // Nothing to assign.
        if(nurses.size() == 0) {
            return false;
        }

        // Do these nurses exist in the system?
        for (Long nurseId :
                nurses) {
            int nurseIndex = allNurses.indexOf(new Employee(nurseId));
            if(nurseIndex == -1) {
                error = "Nurse not found in system.";
                return false;
            }

            if(!BloodDriveService.bloodDrivesContainEmployee(allBds, new Employee(nurseId))) {
                validatedNurses.add(allNurses.get(nurseIndex));
            }
            else {
                return false;
            }
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
