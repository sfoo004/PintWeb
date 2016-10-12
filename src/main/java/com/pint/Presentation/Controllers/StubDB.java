package com.pint.Presentation.Controllers;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserHelper;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Services.BloodDriveService;
import com.pint.BusinessLogic.Services.EmployeeService;
import com.pint.BusinessLogic.Services.HospitalService;
import com.pint.BusinessLogic.Services.NotificationService;
import com.pint.BusinessLogic.Services.UserService;
import  com.pint.Data.Models.*;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;

public class StubDB {
	
	protected Hospital hospital1;
	protected Hospital hospital2;
	protected Hospital hospital3;
	protected Employee testEmployee1;
	protected Employee testEmployee2;
	protected Employee testEmployee3;
	protected Employee testNurse1;
	protected Employee testNurse2;
	protected Employee testNurse3;
	protected Employee testNurse4;
	protected Employee testNurse5;
	protected Employee testNurse6;
	protected User user1;
	protected User user2;
	protected User user3;
	protected User user4;
	protected User user5;
	protected User user6;
	protected User user7;
	protected User user8;
	protected User user9;
	protected User user10;
	protected User user11;
	protected User user12;
	protected Donor testDonor1;
	protected Donor testDonor2;
	protected Donor testDonor3;
	protected BloodDrive bloodDrive1;
	protected BloodDrive bloodDrive2;
	protected BloodDrive bloodDrive3;
	protected Notification notification1;
	protected Notification notification2;
	protected Notification notification3;
	
	protected ControllerFacade controllerFacade;
	protected HospitalRepository hospitalRepository = Mockito.mock(HospitalRepository.class);
	protected UserService userService = Mockito.mock(UserService.class);
	protected BloodDriveService bloodDriveService = Mockito.mock(BloodDriveService.class);
	protected EmployeeService employeeService = Mockito.mock(EmployeeService.class);
	protected EmployeeSummaryViewStrategy employeeSummaryViewStrategy = Mockito.mock(EmployeeSummaryViewStrategy.class);
	protected UserHelper userHelper = Mockito.mock(UserHelper.class);
	protected NotificationService notificationService = Mockito.mock(NotificationService.class);
	protected HospitalService hospitalService = Mockito.mock(HospitalService.class);
	
	protected HospitalController hc = new HospitalController();
	protected BloodDriveController bdc = new BloodDriveController();
	protected EmployeeController ec = new EmployeeController();
	protected UserController uc = new UserController();
	protected Session s = new Session();
	
	//---------------------DATA METHODS------------------------
 	protected BloodDrive createMockBloodDrive(long bloodDriveId, String title, String description, Date startTime, Date endTime, String address, int numberofDonors, String city, String state, int zip, Hospital hospitalId) {
        BloodDrive drive = new BloodDrive(
                bloodDriveId, title, description, startTime, endTime, address, numberofDonors, city, state, zip, hospitalId);

//        when(controllerFacade.getBloodDrivesByLocation(city, state))
//                .thenReturn(drive);

        return drive;
    }
 	
    protected User createMockUser(long id, String username, UserRole role) {
        User user = new User();
        user.setId(id);
        user.grantRole(role);
        user.setUsername(username);
        user.setPassword("password123");
        return user;
    }

    protected Employee createMockEmployee(User user, Hospital hospital) {
        Employee employee = new Employee(user.getId());
        employee.setHospitalId(hospital);
        
        return employee;
    }
    
    protected Notification createMockNotification(long id, String title, String shortDescription, String longDescription, BloodDrive bloodDrive){
    	Notification notification = new Notification(id, title, shortDescription, longDescription);
    	notification.setBloodDrive(bloodDrive);
    	
    	return notification;
    }

    protected Donor createMockDonor(User user) {
        Donor donor = new Donor();
        donor.setUserId(user.getId());

        return donor;
    }
    
    protected Hospital createMockHospital(long hospitalId, String hospitalName) {
        Hospital hospital = new Hospital(hospitalId, hospitalName);
        
        when(hospitalRepository.get(hospitalId))
        .thenReturn(hospital);
        
        return hospital;
    }
//----------------------DATA---------------------------------
   public void createStubDB(){
	   
	   controllerFacade = Mockito.mock(ControllerFacade.class);
	   hc.setHospitalRepository(hospitalRepository);
	   bdc.setBloodDriveService(bloodDriveService);
	   bdc.setUserService(userService);
	   ec.setEmployeeService(employeeService);
	   ec.setUserHelper(userHelper);
	   ec.setUserService(userService);
	   ec.setViewStrategy(employeeSummaryViewStrategy);
	   uc.setHospitalService(hospitalService);
	   uc.setUserService(userService);
	   
	   //create hospitals
	   hospital1 = createMockHospital(1, "FIU Hospital");
	   hospital2 = createMockHospital(2, "MDC Hospital");
	   hospital3 = createMockHospital(3, "UM Hospital");
	   
	   //create Blood Drives
	   bloodDrive1 = createMockBloodDrive(10, "We want your blood", "We are going to take your blood", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "123 Fake Street", 3, "Sweetwater", "Florida", 33139, hospital1);
	   bloodDrive2 = createMockBloodDrive(11, "We want to suck your blood", "We are vampires", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "456  WTF Street", 3, "Hialeah", "Florida", 33167, hospital2);
	   bloodDrive3 = createMockBloodDrive(12, "No blood no love", "We're here to have a bloody good time", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "789 Canes Street", 3, "Coral Gabels", "Florida", 33124, hospital3);
	   
	   //create users
	   user1 = createMockUser(1, "employee1@fiu.edu", UserRole.COORDINATOR);
	   user2 = createMockUser(2, "employee2@fiu.edu", UserRole.COORDINATOR);
	   user3 = createMockUser(3, "employee3@fiu.edu", UserRole.COORDINATOR);
       user4 = createMockUser(4, "nurse1@fiu.edu", UserRole.NURSE);
       user5 = createMockUser(5, "nurse2@fiu.edu", UserRole.NURSE);
       user6 = createMockUser(6, "nurse3@fiu.edu", UserRole.NURSE);
       user7 = createMockUser(7, "nurse4@fiu.edu", UserRole.NURSE);
       user8 = createMockUser(8, "nurse5@fiu.edu", UserRole.NURSE);
       user9 = createMockUser(9, "nurse6@fiu.edu", UserRole.NURSE);
       user10 = createMockUser(10, "donor1@fiu.edu", UserRole.DONOR);
       user11 = createMockUser(11, "donor2@fiu.edu", UserRole.DONOR);
       user12 = createMockUser(12, "donor3@fiu.edu", UserRole.DONOR);
	   
       //create employees
	   testEmployee1 = createMockEmployee(user1, hospital1);
	   testEmployee2 = createMockEmployee(user2, hospital2);
	   testEmployee3 = createMockEmployee(user3, hospital3);
       testNurse1 = createMockEmployee(user4, hospital1);
       testNurse2 = createMockEmployee(user5, hospital2);
       testNurse3 = createMockEmployee(user6, hospital2);
       testNurse4 = createMockEmployee(user7, hospital1);
       testNurse5 = createMockEmployee(user8, hospital2);
       testNurse6 = createMockEmployee(user9, hospital3);
       
       //create donors
	   testDonor1 = createMockDonor(user10);
	   testDonor2 = createMockDonor(user11);
	   testDonor3 = createMockDonor(user12);
       
	   //create notifications
	   notification1 = createMockNotification(1, "MOAR BLOOD", "We need more blood", "Give us more blood", bloodDrive1);
	   notification2 = createMockNotification(2, "WE TOOK TOO MUCH BLOOD", "Someone Donated 10 pints of blood and died", "They were nice enough to donate their organs too :D", bloodDrive2);
	   notification3 = createMockNotification(3, "NO BLOOD NO SWEAT NO TEARS", "More blood = better football team", "We need more blood to make sacrifices for our team", bloodDrive3);
	   
	   when(controllerFacade.getUser(user1)).thenReturn(user1);
	   when(controllerFacade.getUser(user2)).thenReturn(user2);
	   when(controllerFacade.getUser(user3)).thenReturn(user3);
	   when(controllerFacade.getUser(user4)).thenReturn(user4);
	   when(controllerFacade.getUser(user5)).thenReturn(user5);
	   when(controllerFacade.getUser(user6)).thenReturn(user6);
	   when(controllerFacade.getUser(user7)).thenReturn(user7);
	   when(controllerFacade.getUser(user8)).thenReturn(user8);
	   when(controllerFacade.getUser(user9)).thenReturn(user9);
	   when(controllerFacade.getUser(user10)).thenReturn(user10);
	   when(controllerFacade.getUser(user11)).thenReturn(user11);
	   when(controllerFacade.getUser(user12)).thenReturn(user12);
	   
	   when(userService.getUserByEmail("employee1@fiu.edu")).thenReturn(user1);
	   when(userService.getUserByEmail("employee2@fiu.edu")).thenReturn(user2);
	   when(userService.getUserByEmail("employee3@fiu.edu")).thenReturn(user3);
	   when(userService.getUserByEmail("nurse1@fiu.edu")).thenReturn(user4);
	   when(userService.getUserByEmail("nurse2@fiu.edu")).thenReturn(user5);
	   when(userService.getUserByEmail("nurse3@fiu.edu")).thenReturn(user6);
	   when(userService.getUserByEmail("nurse4@fiu.edu")).thenReturn(user7);
	   when(userService.getUserByEmail("nurse5@fiu.edu")).thenReturn(user8);
	   when(userService.getUserByEmail("nurse6@fiu.edu")).thenReturn(user9);
	   
	   List<User> users = new ArrayList<>();
	   users.add(user1);
	   users.add(user2);
	   users.add(user3);
	   users.add(user4);
	   users.add(user5);
	   users.add(user6);
	   users.add(user7);
	   users.add(user8);
	   users.add(user9);
	   users.add(user10);
	   users.add(user11);
	   users.add(user12);
	   
	   when(controllerFacade.list()).thenReturn(users);
//	   when(controllerFacade.userService.getAllUsers()).thenReturn(users);
	   
	   List<Hospital> hospitals = new ArrayList<>();
       hospitals.add(hospital1);
       hospitals.add(hospital2);
       hospitals.add(hospital3);
	   
	   when(hospitalRepository.getHospitals()).thenReturn(hospitals);
	   
	   List<Employee> hospitalNurses1 = new ArrayList<>();
	   hospitalNurses1.add(testNurse1);
	   hospitalNurses1.add(testNurse4);
	   
	   List<Employee> hospitalNurses2 = new ArrayList<>();
	   hospitalNurses2.add(testNurse3);
	   hospitalNurses2.add(testNurse2);
	   hospitalNurses2.add(testNurse5);
	   
	   List<Employee> hospitalNurses3 = new ArrayList<>();
	   hospitalNurses3.add(testNurse6);
	   
	   when(hospitalService.getNurses((long)1)).thenReturn(hospitalNurses1);
	   when(hospitalService.getNurses((long)2)).thenReturn(hospitalNurses2);
	   when(hospitalService.getNurses((long)3)).thenReturn(hospitalNurses3);
	   
	   List<BloodDrive> hialeahBloodDrive = new ArrayList<>();
	   hialeahBloodDrive.add(bloodDrive2);
	   
	   List<BloodDrive> sweetwaterBloodDrive = new ArrayList<>();
	   sweetwaterBloodDrive.add(bloodDrive1);
	   
	   List<BloodDrive> coralGablesBloodDrive = new ArrayList<>();
	   coralGablesBloodDrive.add(bloodDrive3);
	   
	   when(controllerFacade.getBloodDrivesByLocation("Hialeah", "Florida")).thenReturn(hialeahBloodDrive);
	   when(controllerFacade.getBloodDrivesByLocation("SweetWater", "Florida")).thenReturn(sweetwaterBloodDrive);
	   when(controllerFacade.getBloodDrivesByLocation("Coral Gables", "Florida")).thenReturn(coralGablesBloodDrive);
	   
	   List<BloodDrive> bloodDrives = new ArrayList<>();
	   bloodDrives.add(bloodDrive1);
	   bloodDrives.add(bloodDrive2);
	   bloodDrives.add(bloodDrive3);
	   
	   when(controllerFacade.getBloodDrives()).thenReturn(bloodDrives);
	   
	   when(controllerFacade.getBloodDriveByIdForDonor((long)10)).thenReturn(bloodDrive1);
	   when(controllerFacade.getBloodDriveByIdForDonor((long)11)).thenReturn(bloodDrive2);
	   when(controllerFacade.getBloodDriveByIdForDonor((long)12)).thenReturn(bloodDrive3);
	   
   }

}