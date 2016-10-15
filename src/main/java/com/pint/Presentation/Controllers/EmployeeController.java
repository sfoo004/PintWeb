package com.pint.Presentation.Controllers;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserHelper;
import com.pint.BusinessLogic.Services.EmployeeService;
import com.pint.BusinessLogic.Services.UserService;
import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.BusinessLogic.Validators.ValidationException;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController {

    private final Session session;

	@Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeSummaryViewStrategy viewStrategy;

    public EmployeeController() {
        this.session = new Session();
    }

    /**
     * MOCK userService
     * MOCK EmployeeService
     * MOCK user
     * MOCK Employee
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/api/manager/getEmployees")
    @ResponseBody
    public Object getEmployees() throws InterruptedException {
        Iterable<Employee> employees = null;
        try {
            User user = session.getUser();
            if (user.isManager()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                employees = employeeService.getEmployees(user, hospital);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return viewStrategy.CreateViewModel(employees);
    }

    /**
     * MOCK userService
     * MOCK user
     * MOCK Hospital
     * @param newEmployee
     * @return
     */
    @RequestMapping(value = "/api/manager/createEmployee", method = RequestMethod.POST)
    @ResponseBody
    public Object createEmployee(@RequestBody Employee newEmployee) {
        try {
            User user = session.getUser();
            if (user.isManager()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                userService.createEmployee(
                        newEmployee.getEmail(),
                        newEmployee.getPassword(),
                        newEmployee.getFirstName(),
                        newEmployee.getLastName(),
                        newEmployee.getPhoneNumber(),
                        Utils.mapRole(newEmployee.getRole()),
                        hospital.getId());
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (ValidationException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
