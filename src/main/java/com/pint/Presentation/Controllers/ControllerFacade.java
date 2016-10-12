package com.pint.Presentation.Controllers;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserAuthentication;
import com.pint.BusinessLogic.Security.UserHelper;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.BusinessLogic.Validators.ValidationException;
import com.pint.BusinessLogic.Services.*;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Donor;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import com.pint.Data.Models.Notification;
import com.pint.Data.Models.UserNotification;
import com.pint.Data.Repositories.BloodDriveBaseRepository;
import com.pint.Data.Repositories.BloodDriveRepository;
import com.pint.Data.Repositories.DonorRepository;
import com.pint.Data.Repositories.EmployeeRepository;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Data.Repositories.NotificationRepository;
import com.pint.Data.Repositories.UserRepository;
import com.pint.Presentation.ViewStrategies.BloodDriveDetailViewStrategy;
import com.pint.Presentation.ViewStrategies.BloodDriveSummaryViewStrategy;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;
import com.pint.Presentation.ViewStrategies.NotificationDetailViewStrategy;

public class ControllerFacade {
	
	
	@Autowired
	private BloodDriveService bloodDriveService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private NotificationService notificationService;

    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private SecurityContextHolder securityContextHolder;
    
    @Autowired
    private NotificationDetailViewStrategy notificationDetailViewStrategy;
    
    @Autowired
    private BloodDriveSummaryViewStrategy bloodDriveSummaryViewStrategy;
    
    @Autowired
    private Utils utils;
    
    @Autowired
    private EmployeeSummaryViewStrategy viewStrategy;
    
    @Autowired
    private BloodDriveController bloodDriveController;
    
    @Autowired
    private DonorController donorController;
    
    @Autowired
    private EmployeeController employeeController;
    
    @Autowired
    private HospitalController hospitalController;
    
    @Autowired
    private NotificationController notificationController;
    
    @Autowired
    private Session session;
    
    @Autowired
    private UserController userController;
	
//----------------HOSPITAL CONTROLLER -------------------------
	public String createHospital(String hospitalName) {
		return hospitalController.createHospital(hospitalName);
    }
	
	public Hospital getHospital(long hospitalId){
		return new Hospital(hospitalController.getHospital(hospitalId));
	}
	
	public List<Hospital> getHospitals(){
		List<Hospital> hospitalList = hospitalRepository.getHospitals();
		return hospitalList;
	}
	
//----------------USER CONTROLLER --------------------------------
	public void createEmployee(String email, String password, String firstName, String lastName, String phoneNo, String role, long hospitalId){
		userController.createEmployee(email, password, firstName, lastName, phoneNo, role, hospitalId);
	}
	
	public void deleteUser(String username){
		userController.deleteUser(username);
	}
	
	public void updateUser(long id, String email){
		userController.updateUser(id, email);
	}
	
	public Object getNurses(long hospitalId){
		List<Employee> nurses = hospitalService.getNurses(hospitalId);
		return nurses;
	}

	public Object getByEmail(String email){
		return userController.getByEmail(email);
	}
	
	//FIND A WAY TO MOCK THIS???
	public Object getCurrent(){
		final Authentication authentication = securityContextHolder.getContext().getAuthentication();
		User user = ((UserAuthentication) authentication).getDetails();
		if (user.isEmployee()) {
			return userService.getEmployeeByUserId(user.getId());
		}
		return new User(authentication.getName());
	}
	
	public String changePassword(User user){
		final Authentication authentication = securityContextHolder.getContext().getAuthentication();
		userController.changePassword(user);
		final User currentUser = userService.getUserByEmail(authentication.getName());
		if (user.getNewPassword() == null || user.getNewPassword().length() < 4) {
            return "new password too short";
        }
		final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        if (!pwEncoder.matches(user.getPassword(), currentUser.getPassword())) {
            return "old password mismatch";
        }

        currentUser.setPassword(pwEncoder.encode(user.getNewPassword()));
        userService.updateUser(currentUser);
        return "password changed";
	}
	
	public String grantRole(User user, UserRole role){
		if (user == null) {
            return "invalid user id";
        }

        user.grantRole(role);
        userService.updateUser(user);
        return "role granted";
	}
	
	public String revokeRole(User user, UserRole role){
		if (user == null) {
            return "invalid user id";
        }

        user.revokeRole(role);
        userService.updateUser(user);
        return "role revoked";
	}
	
	public Collection<User> list(){
		return userController.list();
	}
	//----------------------------SESSION-----------------------------------

	public User getUser(User user){
		User u = null;
		try {
			
			u = session.getUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u; 
	}
	
//-------------------------- NOTIFICATION CONTROLLER --------------------
	
	public Object getUserNotification(){
		List<UserNotification> un;
		 try {
			 User user = new Session().getUser();
			 if (user.isDonor()) {
             un = notificationService.getUserNotifications(user);
		 } else {
             return "Unauthorized";
         }
		 } catch (Exception ex) {
			 return "BadRequest";
		 }
		 return notificationDetailViewStrategy.CreateViewModel(un);
	}
	
	public Object getUserNotifications(long id){
		List<UserNotification> un;
		try {
            User user = new Session().getUser();
            if (user.isDonor()) {
                un = notificationService.getUserNotifications(user, id);
            } else {
                return "Unauthorized";
            }
        } catch (Exception ex) {
            return "BadRequest";
        }

        return notificationDetailViewStrategy.CreateViewModel(un);
	}
	
//----------------------- EMPLOYEE CONTROLLER --------------------------------
	
	public Object getEmployees(){
		Iterable<Employee> employees = null;
		try{
			User user = new Session().getUser();
			if (user.isManager()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                employees = employeeService.getEmployees(user, hospital);
            } else {
                return "Unauthorized";
            }
        } catch (Exception ex) {
            return "BadRequest";
        }

        return viewStrategy.CreateViewModel(employees);
	}

	public Object createEmployee(Employee newEmployee){
		try{
			User user = new Session().getUser();
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
                return "Unauthorized";
            }
        } catch (Exception ex) {
            return "BadRequest";
        }


        return "EmployeeCreated";
	}
//------------------BLOOD DRIVE CONTROLLER --------------------
	
	public Object getBloodDrivesByLocation(String city, String state){
		return bloodDriveController.getBloodDrivesByLocation(city, state);
		
	}
	
	public Object getBloodDriveByIdForDonor(Long id){
		return bloodDriveController.getBloodDriveByIdForDonor(id);
	}
	
	public Object getBloodDrives(){
		Object bloodDriveList = null;
		try {
			
			bloodDriveList =  bloodDriveController.getBloodDrives();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bloodDriveList;
		
//		List<BloodDrive> bloodDrives = null;
//        try {
//            User user = new Session().getUser();
//            if (user.isCoordinator()) {
//                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
//                bloodDrives = bloodDriveService.getBloodDrivesForCoordinator(hospital, user);
//            } 
//        } catch (Exception ex) {
//            return "BadRequest";
//        }
//
//        return bloodDriveSummaryViewStrategy.CreateViewModel(bloodDrives);
	}
	
	public Object getBloodDrivesForNurse(){
		
		List<BloodDrive> bloodDrives = null;
        try {
            User user = new Session().getUser();
            if (user.isNurse()) {
                Hospital hospital = userService.getEmployeeByUserId(user.getId()).getHospitalId();
                bloodDrives = bloodDriveService.getBloodDrivesForNurse(hospital, user);
            } 
        } catch (Exception ex) {
            return "BadQuest";
        }
        return bloodDriveSummaryViewStrategy.CreateViewModel(bloodDrives);
	}
	
	public Object getBloodDriveByIdForCoordinator(Long bdId){
		BloodDrive bd = null;
        List<Employee> assignedNurses = null;
        List<Employee> unassignedNurses = null;
        try {
            User user = new Session().getUser();
            if (user.isCoordinator()) {
                bd = bloodDriveService.getBloodDriveByCoordinator(bdId, user);
                assignedNurses = bloodDriveService.getNursesForBloodDrive(bdId, user);
                unassignedNurses = bloodDriveService.getUnassignedNurses(bdId, user);
            } 
        } catch (Exception ex) {
            return "BadRequest";
        }
        //MAY NEED TO MOCK THIS
        return new BloodDriveDetailViewStrategy(assignedNurses, unassignedNurses).CreateViewModel(bd);
	}
	
	public Object getBloodDriveByIdForNurse(long bdId){
		BloodDrive bd = null;
        Employee coordinator = null;
        try {
            User user = new Session().getUser();
            if (user.isNurse()) {
                bd = bloodDriveService.getBloodDriveByNurse(bdId, user);
                coordinator = bloodDriveService.getCoordinator(bd);
            }
        } catch (Exception ex) {
            return "BadRequest";
        }
      //MAY NEED TO MOCK THIS
        return new BloodDriveDetailViewStrategy(
                new EmployeeSummaryViewStrategy().CreateViewModel(coordinator))
                .CreateViewModel(bd);
	}
	
	public Object inputDonor(long bdId, String email){
		try {
            User user = new Session().getUser();
            if (user.isNurse()) {
                bloodDriveService.inputDonor(user, bdId, email);
            } 
        } catch (ValidationException ex) {
            return "BadRequest";
        } catch (Exception ex) {
            return "BadRequest";
        }

        return bdId;
	}

	public Object assignNurses(long bdId, ArrayList<Integer> nurses){
		 try {
	            User user = new Session().getUser();
	            if (user.isCoordinator()) {
	                bloodDriveService.assignNurses(user, bdId,
	                        Utils.toLongs(nurses));
	            }
	        } catch (Exception ex) {
	            return "BadRequest";
	        }

	        return bdId;
	}
	
	public Object unassignNurses(long bdId, ArrayList<Integer> nurses){
		 try {
	            User user = new Session().getUser();
	            if (user.isCoordinator()) {
	                bloodDriveService.unassignNurses(user, bdId,
	                        Utils.toLongs(nurses));
	            } 
	        } catch (Exception ex) {
	            return "BadRequest";
	        }

	        return bdId;
	}  
	
}
