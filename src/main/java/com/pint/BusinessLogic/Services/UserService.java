package com.pint.BusinessLogic.Services;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Validators.CreateEmployeeValidator;
import com.pint.BusinessLogic.Validators.ValidationException;
import com.pint.BusinessLogic.Validators.Validator;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.Donor;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dionny on 11/24/2015.
 */
@Service
public class UserService {

    @Autowired
    private DataFacade dataFacade;

    public UserService() {
    }

    public UserService(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public Employee createEmployee(String email,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   String phoneNo,
                                   UserRole role,
                                   Long hospitalId) throws Exception {

        User user = new User();
        user.setUsername(email);

        Hospital hospital = dataFacade.getHospitalById(hospitalId);
        Employee employee = new Employee(firstName, lastName, phoneNo, hospital);
        employee.setPassword(password);

        Iterable<User> users = getAllUsers();

        Validator validator = new CreateEmployeeValidator(users, user, employee, role);
        if (!validator.Validate()) {
            throw new ValidationException(validator.getError());
        }

        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.grantRole(role);
        dataFacade.createOrUpdateUser(user);
        employee.setUserId(user.getId());
        dataFacade.createEmployee(employee);

        return employee;
    }

    public Donor createDonor(String username,
                             String password,
                             String country,
                             String city,
                             String state,
                             int zip) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.grantRole(UserRole.DONOR);

        dataFacade.createOrUpdateUser(user);

        Donor donor = new Donor(country, city, state, zip);
        donor.setUserId(user.getId());

        dataFacade.createDonor(donor);

        return donor;
    }

    public User getUserById(Long id) {
        return dataFacade.getUserById(id);
    }

    public User getUserByEmail(String email) {
        return dataFacade.getUserByEmail(email);
    }

    public void deleteUser(String username) {
        dataFacade.deleteUser(username);
    }

    public User updateUser(long id, String email) {
        User user = dataFacade.getUserById(id);
        user.setUsername(email);
        dataFacade.createOrUpdateUser(user);
        return user;
    }

    public Employee getEmployeeByUserId(Long id) {
        return dataFacade.getEmployeeById(id);
    }

    public void updateUser(User currentUser) {
        dataFacade.createOrUpdateUser(currentUser);
    }

    public Iterable<User> getAllUsers() {
        return dataFacade.getAllUsers();
    }
}
