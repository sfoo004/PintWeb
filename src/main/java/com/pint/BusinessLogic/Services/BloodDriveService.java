package com.pint.BusinessLogic.Services;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Validators.*;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dionny on 11/24/2015.
 */
@Service
public class BloodDriveService {

    @Autowired
    private UserService userService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DataFacade dataFacade;

    public BloodDriveService(){
    }

    public BloodDriveService(UserService userService,
                             HospitalService hospitalService,
                             DataFacade dataFacade) {
        this.userService = userService;
        this.hospitalService = hospitalService;
        this.dataFacade = dataFacade;
    }

    public Employee getCoordinator(BloodDrive bd) {
        Set<Employee> employees = bd.getEmployees();
        for (Employee ee :
                employees) {
            User user = userService.getUserById(ee.getUserId());
            if (user.hasRole(UserRole.COORDINATOR)) {
                return ee;
            }
        }
        return null;
    }

    public List<BloodDrive> getBloodDrivesByHospital(long hospitalId) {
        List<BloodDrive> output = new ArrayList<>();
        Iterable<BloodDrive> allBds = dataFacade.getBloodDrives();
        for (BloodDrive bd :
                allBds) {
            if (bd.getHospitalId().getId() == hospitalId) {
                output.add(bd);
            }
        }
        return output;
    }

    public List<BloodDrive> getBloodDrivesByLocation(String city, String state) {
        return dataFacade.getBloodDrivesByLocation(city, state);
    }

    public List<BloodDrive> getBloodDrivesForCoordinator(Hospital hospital, User user) {
        List<BloodDrive> output = new ArrayList<>();
        Iterable<BloodDrive> bloodDrives = dataFacade.getBloodDrives();
        for (BloodDrive bloodDrive :
                bloodDrives) {
            if (bloodDrive.getHospitalId().getId() == hospital.getId() &&
                    getCoordinator(bloodDrive) != null &&
                    getCoordinator(bloodDrive).getUserId() == user.getId()) {
                output.add(bloodDrive);
            }
        }

        return output;
    }

    public BloodDrive createBloodDrive(Hospital hospital,
                                       String title,
                                       String address,
                                       String description,
                                       String city,
                                       String state,
                                       Date from,
                                       Date to,
                                       Employee coordinator) {

        BloodDrive bloodDrive = new BloodDrive();
        bloodDrive.setTitle(title);
        bloodDrive.setAddress(address);
        bloodDrive.setDescription(description);
        bloodDrive.setStartTime(from);
        bloodDrive.setEndTime(to);
        bloodDrive.setCity(city);
        bloodDrive.setState(state);
        bloodDrive.setHospitalId(hospital);

        setCoordinator(bloodDrive, coordinator);

        dataFacade.createOrUpdateBloodDrive(bloodDrive);

        return bloodDrive;
    }

    private void setCoordinator(BloodDrive bd, Employee coordinator) {
        Set<Employee> employees = bd.getEmployees();
        employees.add(coordinator);
    }

    public BloodDrive getBloodDriveByCoordinator(long bdId, User user) {
        BloodDrive bd = dataFacade.getBloodDriveById(bdId);
        Employee coordinator = getCoordinator(bd);
        if (coordinator != null && coordinator.getUserId() == user.getId()) {
            return bd;
        }
        return null;
    }

    public List<Employee> getNursesForBloodDrive(Long bdId, User user) {
        List<Employee> output = new ArrayList<>();
        BloodDrive bd = dataFacade.getBloodDriveById(bdId);
        Employee coordinator = getCoordinator(bd);
        if (coordinator != null && coordinator.getUserId() == user.getId()) {
            Set<Employee> employees = bd.getEmployees();
            for (Employee ee :
                    employees) {
                User eeUser = userService.getUserById(ee.getUserId());
                if (eeUser.hasRole(UserRole.NURSE)) {
                    ee.setEmail(eeUser.getUsername());
                    output.add(ee);
                }
            }
        }

        return output;
    }

    public static boolean bloodDrivesContainEmployee(List<BloodDrive> bloodDrives, Employee employee) {
        for (BloodDrive drive :
                bloodDrives) {
            if (drive.getEmployees().contains(employee)) {
                return true;
            }
        }
        return false;
    }

    public List<Employee> getUnassignedNurses(Long bdId, User user) {
        BloodDrive bd = dataFacade.getBloodDriveById(bdId);
        List<BloodDrive> allBds = getBloodDrivesByHospital(bd.getHospitalId().getId());
        List<Employee> allNurses = hospitalService.getNurses(bd.getHospitalId().getId());
        List<Employee> output = new ArrayList<>();

        Employee coordinator = getCoordinator(bd);
        if (coordinator != null && coordinator.getUserId() == user.getId()) {
            for (Employee ee :
                    allNurses) {
                if (!bloodDrivesContainEmployee(allBds, ee)) {
                    output.add(ee);
                }
            }
        }

        return output;
    }

    public void assignNurses(User user, Long bdId, List<Long> nurses) throws Exception {
        BloodDrive drive = dataFacade.getBloodDriveById(bdId);
        List<BloodDrive> allBds = getBloodDrivesByHospital(drive.getHospitalId().getId());
        List<Employee> allNurses = hospitalService.getNurses(drive.getHospitalId().getId());
        Employee coordinator = getCoordinator(drive);

        NurseAssignmentValidator validator = new
                NurseAssignmentValidator(
                user, drive, coordinator, nurses, allNurses, allBds);

        if (!validator.Validate()) {
            throw new Exception("Invalid request.");
        }

        List<Employee> nursesToAssign = validator.getValidatedObjects();
        drive.getEmployees().addAll(nursesToAssign);
        dataFacade.createOrUpdateBloodDrive(drive);
    }

    public void unassignNurses(User user, Long bdId, List<Long> nurses) throws Exception {
        BloodDrive drive = dataFacade.getBloodDriveById(bdId);
        Employee coordinator = getCoordinator(drive);

        NurseUnassignmentValidator validator = new
                NurseUnassignmentValidator(
                user, coordinator, nurses);

        if (!validator.Validate()) {
            throw new Exception("Invalid request.");
        }

        ArrayList<Employee> nursesToUnassign = validator.getValidatedObjects();
        drive.getEmployees().removeAll(nursesToUnassign);
        dataFacade.createOrUpdateBloodDrive(drive);
    }

    public List<BloodDrive> getBloodDrivesForNurse(Hospital hospital, User user) {
        List<BloodDrive> output = new ArrayList<>();
        Iterable<BloodDrive> bloodDrives = dataFacade.getBloodDrives();
        for (BloodDrive bloodDrive :
                bloodDrives) {
            if (bloodDrive.getHospitalId().getId() == hospital.getId()) {
                Set<Employee> employees = bloodDrive.getEmployees();
                if (employees.contains(new Employee(user.getId()))) {
                    output.add(bloodDrive);
                }
            }
        }

        return output;
    }

    public BloodDrive getBloodDriveByNurse(long bdId, User user) {
        BloodDrive drive = dataFacade.getBloodDriveById(bdId);
        Set<Employee> employees = drive.getEmployees();
        if (employees.contains(new Employee(user.getId()))) {
            return drive;
        }

        return null;
    }

    public void inputDonor(User user, Long bdId, String email) throws ValidationException {
        BloodDrive drive = dataFacade.getBloodDriveById(bdId);
        Validator validator = new InputDonorValidator(user, userService, email, drive);
        if (validator.Validate()) {
            drive.numberOfDonors++;
            dataFacade.createOrUpdateBloodDrive(drive);
        } else {
            throw new ValidationException(validator.getError());
        }
    }

    public BloodDrive getBloodDriveById(Long id) {
        return dataFacade.getBloodDriveById(id);
    }
}
