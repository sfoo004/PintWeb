package com.pint.Presentation.Controllers;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Services.BloodDriveService;
import com.pint.BusinessLogic.Services.UserService;
import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.BusinessLogic.Validators.ValidationException;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import com.pint.Presentation.ViewStrategies.BloodDriveDetailViewStrategy;
import com.pint.Presentation.ViewStrategies.BloodDriveSummaryViewStrategy;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BloodDriveController {

    @Autowired
    private BloodDriveService bloodDriveService;

    @Autowired
    private UserService userService;

    private UserProvider session;

    public BloodDriveController() {
        this.session = new Session();
    }

    /**
     * MOCK bloodDriveService
     * MOCK UserService
     * @param bloodDriveService
     * @param userService
     * @param provider
     */
    public BloodDriveController(BloodDriveService bloodDriveService, UserService userService, UserProvider provider) {
        this.bloodDriveService = bloodDriveService;
        this.userService = userService;
        this.session = provider;
    }

    /**
     * MOCK BloodDriveSummaryViewStrategy
     * MOCK BloodDrive
     * @param city
     * @param state
     * @return
     */
    @RequestMapping("/api/donor/getBloodDrivesByLocation/{city}/{state}")
    @ResponseBody
    public Object getBloodDrivesByLocation(@PathVariable("city") String city, @PathVariable("state") String state) {
        List<BloodDrive> bloodDrives = null;
        try {
            bloodDrives = bloodDriveService.getBloodDrivesByLocation(city, state);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveSummaryViewStrategy().CreateViewModel(bloodDrives);
    }

    /**
     * MOCK BloodDrive
     * MOCK bloodDriveService
     * @param id
     * @return
     */
    @RequestMapping("/api/donor/getBloodDrive/{id}")
    @ResponseBody
    public Object getBloodDriveByIdForDonor(@PathVariable("id") Long id) {
        BloodDrive bloodDrive = null;
        try {
            bloodDrive = bloodDriveService.getBloodDriveById(id);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveDetailViewStrategy().CreateViewModel(bloodDrive);
    }

    /**
     * MOCK BloodDrive
     * MOCK User
     * MOCK Hospital
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/api/coordinator/getBloodDrives")
    @ResponseBody
    public Object getBloodDrives() throws InterruptedException {
        List<BloodDrive> bloodDrives = null;
        try {
            User user = session.getUser();
            if (user.isCoordinator()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                bloodDrives = bloodDriveService.getBloodDrivesForCoordinator(hospital, user);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveSummaryViewStrategy().CreateViewModel(bloodDrives);
    }

    /**
     * MOCK BloodDrive
     * MOCK User
     * MOCK Hospital
     * MOCK bloodDrive
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/api/nurse/getBloodDrives")
    @ResponseBody
    public Object getBloodDrivesForNurse() throws InterruptedException {
        List<BloodDrive> bloodDrives = null;
        try {
            User user = session.getUser();
            if (user.isNurse()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                bloodDrives = bloodDriveService.getBloodDrivesForNurse(hospital, user);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveSummaryViewStrategy().CreateViewModel(bloodDrives);
    }

    /**
     * MOCK Employee
     * MOCK User
     * MOCK bloodDriveService
     * MOCK BloodDrive
     * @param bdId Long
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/api/coordinator/getBloodDriveById/{id}")
    @ResponseBody
    public Object getBloodDriveByIdForCoordinator(@PathVariable("id") Long bdId) throws InterruptedException {
        BloodDrive bd;
        List<Employee> assignedNurses;
        List<Employee> unassignedNurses;
        try {
            User user = session.getUser();
            if (user.isCoordinator()) {
                bd = bloodDriveService.getBloodDriveByCoordinator(bdId, user);
                assignedNurses = bloodDriveService.getNursesForBloodDrive(bdId, user);
                unassignedNurses = bloodDriveService.getUnassignedNurses(bdId, user);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveDetailViewStrategy(assignedNurses, unassignedNurses).CreateViewModel(bd);
    }

    /**
     * MOCK Employee
     * MOCK BloodDrive
     * MOCK bloodDriveService
     * @param bdId long
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/api/nurse/getBloodDriveById/{id}")
    @ResponseBody
    public Object getBloodDriveByIdForNurse(@PathVariable("id") long bdId) throws InterruptedException {
        BloodDrive bd;
        Employee coordinator;
        try {
            User user = session.getUser();
            if (user.isNurse()) {
                bd = bloodDriveService.getBloodDriveByNurse(bdId, user);
                coordinator = bloodDriveService.getCoordinator(bd);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new BloodDriveDetailViewStrategy(
                new EmployeeSummaryViewStrategy().CreateViewModel(coordinator))
                .CreateViewModel(bd);
    }

    /**
     * MOCK user
     * MOCK bloodDriveService
     * @param bdId
     * @param email
     * @return
     */
    @RequestMapping(value = "/api/nurse/inputDonor/{bdId}", method = RequestMethod.POST)
    @ResponseBody
    public Object inputDonor(@PathVariable("bdId") Long bdId, @RequestBody String email) {
        try {
            User user = session.getUser();
            if (user.isNurse()) {
                bloodDriveService.inputDonor(user, bdId, email);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (ValidationException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return bdId;
    }

    /**
     * MOCK user
     * MOCK bloodDriveService
     * @param bdId
     * @param nurses
     * @return
     */
    @RequestMapping(value = "/api/coordinator/assignNurses/{bdId}", method = RequestMethod.POST)
    @ResponseBody
    public Object assignNurses(@PathVariable("bdId") Long bdId, @RequestBody ArrayList<Integer> nurses) {
        try {
            User user = session.getUser();
            if (user.isCoordinator()) {
                bloodDriveService.assignNurses(user, bdId,
                        Utils.toLongs(nurses));
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return bdId;
    }

    /**
     * MOCK user
     * MOCK bloodDriveService
     * @param bdId
     * @param nurses
     * @return
     */
    @RequestMapping(value = "/api/coordinator/unassignNurses/{bdId}", method = RequestMethod.POST)
    @ResponseBody
    public Object unassignNurses(@PathVariable("bdId") Long bdId, @RequestBody ArrayList<Integer> nurses) {
        try {
            User user = session.getUser();
            if (user.isCoordinator()) {
                bloodDriveService.unassignNurses(user, bdId,
                        Utils.toLongs(nurses));
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return bdId;
    }
}
