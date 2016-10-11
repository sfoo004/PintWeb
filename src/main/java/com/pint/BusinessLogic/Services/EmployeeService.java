package com.pint.BusinessLogic.Services;

import com.pint.BusinessLogic.Security.User;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dionny on 11/27/2015.
 */
@Service
public class EmployeeService {
    @Autowired
    private DataFacade dataFacade;

    @Autowired
    private UserService userService;

    public EmployeeService() {
    }

    public EmployeeService(UserService service, DataFacade dataFacade) {
        userService = service;
        this.dataFacade = dataFacade;
    }

    public List<Employee> getEmployees(User user, Hospital hospital) {
        List<Employee> output = new ArrayList<>();
        Iterable<Employee> employees = dataFacade.getHospitalEmployees(hospital.getId());

        for (Employee ee :
                employees) {

            if(user.getId() == ee.getUserId()){
                continue;
            }

            User eeUser = userService.getUserById(ee.getUserId());
            ee.setEmail(eeUser.getUsername());
            ee.setRole(String.valueOf(eeUser.getRoles().toArray()[0]));
            output.add(ee);
        }

        return output;
    }
}
