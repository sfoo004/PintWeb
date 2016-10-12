package com.pint.Presentation.Controllers;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserAuthentication;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Services.HospitalService;
import com.pint.BusinessLogic.Services.UserService;
import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.Data.Models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {
    private final AtomicLong counter = new AtomicLong();

    /**
     * MOCK userService 
     * Creates new employee. Sends the information to the userService
     * @param email String
     * @param password String
     * @param firstName String
     * @param lastName String 
     * @param phoneNo String
     * @param role String 
     * @param hospitalId long
     * @return
     */
    @RequestMapping("/api/manager/createEmployee")
    @ResponseBody
    public Object createEmployee(String email, String password, String firstName, String lastName, String phoneNo, String role, long hospitalId) {
        Employee employee = null;
        try {
            employee = userService.createEmployee(email, password, firstName, lastName, phoneNo, UserRole.valueOf(role.toUpperCase()), hospitalId);
        } catch (Exception ex) {
            return "Error creating employee: " + ex.toString();
        }
        return employee;
    }

    /**
     * MOCK userService 
     * GET /api/manager/deleteUser  --> Delete the user having the passed id.
     */
    @RequestMapping("/api/manager/deleteUser")
    @ResponseBody
    public String deleteUser(String username) {
        try {
            userService.deleteUser(username);
        } catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User succesfully deleted!";
    }
    
    /**
     * MOCK userService 
     * GET /api/manager/updateUser  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @RequestMapping("/api/manager/updateUser")
    @ResponseBody
    public Object updateUser(long id, String email) {
        try {
            return userService.updateUser(id, email);
        } catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
    }

    /**
     * MOCK hospitalService
     * @param hospitalId
     * @return
     */
    @RequestMapping("/api/coordinator/getNurses")
    @ResponseBody
    public Object getNurses(long hospitalId) {
        List<Employee> nurses = null;

        try {
            nurses = hospitalService.getNurses(hospitalId);
        } catch (Exception ex) {
            return "Error getting nurses\n";
        }

        return nurses;
    }

    /** 
     * MOCK userService
     * GET /api/manager/getUsersByEmail  --> Return the id for the user having the passed
     * email.
     */
    @RequestMapping("/api/manager/getUsersByEmail")
    @ResponseBody
    public Object getByEmail(String email) {
        User user = null;
        try {
            user = userService.getUserByEmail(email);
        } catch (Exception ex) {
            return "User not found";
        }
        return user;
    }

    /**
     * MOCK SecurityContextHolder?? Spring
     * MOCK Authentication?? Spring
     * MOCK userService
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/api/users/current", method = RequestMethod.GET)
    public Object getCurrent() throws InterruptedException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            User user = ((UserAuthentication) authentication).getDetails();
            if (user.isEmployee()) {
                return userService.getEmployeeByUserId(user.getId());
            }
        }
        return new User(authentication.getName()); //anonymous user support
    }
    
    /**
     * MOCK userService
     * MOCK SecurityContextHolder?? Spring
     * MOCK Authentication?? Spring
     * MOCK BCryptPasswordEncoder?? Spring
     * @param user User
     * @return
     */
    @RequestMapping(value = "/api/users/current", method = RequestMethod.PATCH)
    public ResponseEntity<String> changePassword(@RequestBody final User user) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User currentUser = userService.getUserByEmail(authentication.getName());

        if (user.getNewPassword() == null || user.getNewPassword().length() < 4) {
            return new ResponseEntity<String>("new password to short", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        if (!pwEncoder.matches(user.getPassword(), currentUser.getPassword())) {
            return new ResponseEntity<String>("old password mismatch", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        currentUser.setPassword(pwEncoder.encode(user.getNewPassword()));
        userService.updateUser(currentUser);
        return new ResponseEntity<String>("password changed", HttpStatus.OK);
    }

    /**
     * MOCK userService
     * @param user
     * @param role
     * @return
     */
    @RequestMapping(value = "/api/manager/users/{user}/grant/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<String> grantRole(@PathVariable User user, @PathVariable UserRole role) {
        if (user == null) {
            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        user.grantRole(role);
        userService.updateUser(user);
        return new ResponseEntity<String>("role granted", HttpStatus.OK);
    }
    
    /**
     * MOCK userService
     * @param user
     * @param role
     * @return
     */
    @RequestMapping(value = "/api/manager/users/{user}/revoke/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<String> revokeRole(@PathVariable User user, @PathVariable UserRole role) {
        if (user == null) {
            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        user.revokeRole(role);
        userService.updateUser(user);
        return new ResponseEntity<String>("role revoked", HttpStatus.OK);
    }

    /**
     * MOCK Utils
     * @return
     */
    @RequestMapping(value = "/api/manager/users", method = RequestMethod.GET)
    public Collection<User> list() {
        return Utils.iterableToCollection(userService.getAllUsers());
    }

    // Private fields

    @Autowired
    private UserService userService;
    
	@Autowired
    private HospitalService hospitalService;

    public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public HospitalService getHospitalService() {
		return hospitalService;
	}

	public void setHospitalService(HospitalService hospitalService) {
		this.hospitalService = hospitalService;
	}
}