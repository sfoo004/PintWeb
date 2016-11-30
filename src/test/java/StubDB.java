
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mockito.Mockito;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserAuthentication;
import com.pint.BusinessLogic.Security.UserHelper;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Services.BloodDriveService;
import com.pint.BusinessLogic.Services.EmployeeService;
import com.pint.BusinessLogic.Services.HospitalService;
import com.pint.BusinessLogic.Services.NotificationService;
import com.pint.BusinessLogic.Services.UserService;
import  com.pint.Data.Models.*;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Presentation.Controllers.BloodDriveController;
import com.pint.Presentation.Controllers.EmployeeController;
import com.pint.Presentation.Controllers.HospitalController;
import com.pint.Presentation.Controllers.NotificationController;
import com.pint.Presentation.Controllers.Session;
import com.pint.Presentation.Controllers.UserController;
import com.pint.Presentation.Controllers.UserProvider;
import com.pint.Presentation.ViewModels.EmployeeSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;

@RunWith(MockitoJUnitRunner.class)
public class StubDB {
	
	//----------- MOCKED SERVICES -----------------
	@Mock
	protected HospitalRepository hospitalRepository;
	
	@Mock
	protected UserService userService;
	
	@Mock
	protected BloodDriveService bloodDriveService;

	@Mock
	protected EmployeeService employeeService;
	
	@Mock
	protected EmployeeSummaryViewStrategy employeeSummaryViewStrategy;
	
	@Mock
	protected UserHelper userHelper;
	
	@Mock
	protected NotificationService notificationService;
	
	@Mock
	protected HospitalService hospitalService;
	
	@Mock
	protected UserProvider session;
	
	@Mock
	protected Session s;
	
	@Mock
	protected BCryptPasswordEncoder passwordEncoder;
	
	//------------------ INJECTED MOCKED SERVES INTO CONTROLLERS-------------------
	@InjectMocks
	protected HospitalController hc = new HospitalController();
	
	@InjectMocks
	protected BloodDriveController bdc = new BloodDriveController();

	@InjectMocks
	protected EmployeeController ec;
	
	@InjectMocks
	protected UserController uc = new UserController();
	
	@InjectMocks
	protected NotificationController n;
	
	@InjectMocks
	protected Session ses;
	
	//Variables in stub DB
	protected Hospital hospital1;
	protected Hospital hospital2;
	protected Hospital hospital3;
	protected Employee testCoordinator1;
	protected Employee testCoordinator2;
	protected Employee testCoordinator3;
	protected Employee testNurse1;
	protected Employee testNurse2;
	protected Employee testNurse3;
	protected Employee testNurse4;
	protected Employee testNurse5;
	protected Employee testNurse6;
	protected Employee testManager1;
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
	protected User user13;
	protected Donor testDonor1;
	protected Donor testDonor2;
	protected Donor testDonor3;
	protected BloodDrive bloodDrive1;
	protected BloodDrive bloodDrive2;
	protected BloodDrive bloodDrive3;
	protected Notification notification1;
	protected Notification notification2;
	protected Notification notification3;
	protected UserAuthentication userAuthenticated1;
	protected UserAuthentication userAuthenticated2;
	protected UserAuthentication userAuthenticated3;
	protected UserAuthentication userAuthenticated4;
	protected UserAuthentication userAuthenticated5;
	protected UserAuthentication userAuthenticated6;
	protected UserAuthentication userAuthenticated7;
	protected UserAuthentication userAuthenticated8;
	protected UserAuthentication userAuthenticated9;
	protected UserAuthentication userAuthenticated10;
	protected UserAuthentication userAuthenticated11;
	protected UserAuthentication userAuthenticated12;
	protected List<BloodDrive> hialeahBloodDrive = new ArrayList<>();
	protected List<BloodDrive> sweetwaterBloodDrive = new ArrayList<>();
	protected List<BloodDrive> coralGablesBloodDrive = new ArrayList<>();
	protected List<Employee> hospitalNurses1 = new ArrayList<>();
	protected List<Employee> hospitalNurses2 = new ArrayList<>();
	protected List<Employee> hospitalNurses3 = new ArrayList<>();
	protected List<Employee> notHospitalNurses1 = new ArrayList<>();
	protected List<Employee> notHospitalNurses2 = new ArrayList<>();
	protected List<Employee> notHospitalNurses3 = new ArrayList<>();
	
	
	//---------------------STUB DATABASE METHODS------------------------
 	protected BloodDrive createMockBloodDrive(long bloodDriveId, String title, String description, Date startTime, Date endTime, String address, int numberofDonors, String city, String state, int zip, Hospital hospitalId) {
        BloodDrive drive = new BloodDrive(
                bloodDriveId, title, description, startTime, endTime, address, numberofDonors, city, state, zip, hospitalId);

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
        employee.setEmail(user.getUsername());
        
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
	   
	   //create mock hospitals
	   hospital1 = createMockHospital(1, "FIU Hospital");
	   hospital2 = createMockHospital(2, "MDC Hospital");
	   hospital3 = createMockHospital(3, "UM Hospital");
	   
	   //create mock Blood Drives
	   bloodDrive1 = createMockBloodDrive(10, "We want your blood", "We are going to take your blood", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "123 Fake Street", 3, "Sweetwater", "Florida", 33139, hospital1);
	   bloodDrive2 = createMockBloodDrive(11, "We want to suck your blood", "We are vampires", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "456  WTF Street", 3, "Hialeah", "Florida", 33167, hospital2);
	   bloodDrive3 = createMockBloodDrive(12, "No blood no love", "We're here to have a bloody good time", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+(86400000*2)), "789 Canes Street", 3, "Coral Gabels", "Florida", 33124, hospital3);
	   
	   //create mock users
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
       user13 = createMockUser(13, "manager1@fiu.edu", UserRole.MANAGER);
	   
       //create mock employees
	   testCoordinator1 = createMockEmployee(user1, hospital1);
	   testCoordinator1.setRole("COORDINATOR");
	   testCoordinator2 = createMockEmployee(user2, hospital2);
	   testCoordinator2.setRole("COORDINATOR");
	   testCoordinator3 = createMockEmployee(user3, hospital3);
	   testCoordinator3.setRole("COORDINATOR");
       testNurse1 = createMockEmployee(user4, hospital1);
       testNurse1.setRole("NURSE");
       testNurse2 = createMockEmployee(user5, hospital2);
       testNurse2.setRole("NURSE");
       testNurse3 = createMockEmployee(user6, hospital2);
       testNurse3.setRole("NURSE");
       testNurse4 = createMockEmployee(user7, hospital1);
       testNurse4.setRole("NURSE");
       testNurse5 = createMockEmployee(user8, hospital2);
       testNurse5.setRole("NURSE");
       testNurse6 = createMockEmployee(user9, hospital3);
       testNurse6.setRole("NURSE");
       testManager1 = createMockEmployee(user13, hospital1);
       testManager1.setRole("MANAGER");
       
       //create mock donors
	   testDonor1 = createMockDonor(user10);
	   testDonor2 = createMockDonor(user11);
	   testDonor3 = createMockDonor(user12);
       
	   //create mock notifications
	   notification1 = createMockNotification(1, "MOAR BLOOD", "We need more blood", "Give us more blood", bloodDrive1);
	   notification2 = createMockNotification(2, "WE TOOK TOO MUCH BLOOD", "Someone Donated 10 pints of blood and died", "They were nice enough to donate their organs too :D", bloodDrive2);
	   notification3 = createMockNotification(3, "NO BLOOD NO SWEAT NO TEARS", "More blood = better football team", "We need more blood to make sacrifices for our team", bloodDrive3);
	   
	   //create user Authenticated users
	   userAuthenticated1 = new UserAuthentication(user1);
	   userAuthenticated2 = new UserAuthentication(user2);
	   userAuthenticated3 = new UserAuthentication(user3);
	   userAuthenticated4 = new UserAuthentication(user4);
	   userAuthenticated5 = new UserAuthentication(user5);
	   userAuthenticated6 = new UserAuthentication(user6);
	   userAuthenticated7 = new UserAuthentication(user7);
	   userAuthenticated8 = new UserAuthentication(user8);
	   userAuthenticated9 = new UserAuthentication(user9);
	   userAuthenticated10 = new UserAuthentication(user10);
	   userAuthenticated11 = new UserAuthentication(user11);
	   userAuthenticated12 = new UserAuthentication(user12);
	   
	   //get user by Email returns mock user
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
	   users.add(user13);
	   
	   //list all the mock users from a list
	   when(userService.getAllUsers()).thenReturn(users);
	   
	   List<Hospital> hospitals = new ArrayList<>();
       hospitals.add(hospital1);
       hospitals.add(hospital2);
       hospitals.add(hospital3);
	   
       //returns all the mock hospitals from a list
	   when(hospitalRepository.getHospitals()).thenReturn(hospitals);
	   
	   hospitalNurses1.add(testNurse1);
	   hospitalNurses1.add(testNurse4);
	   
	   hospitalNurses2.add(testNurse3);
	   hospitalNurses2.add(testNurse2);
	   hospitalNurses2.add(testNurse5);
	   
	   hospitalNurses3.add(testNurse6);
	   
	   notHospitalNurses1.add(testNurse3);
	   notHospitalNurses1.add(testNurse2);
	   notHospitalNurses1.add(testNurse5);
	   notHospitalNurses1.add(testNurse6);
	      
	   notHospitalNurses2.add(testNurse6);
	   notHospitalNurses2.add(testNurse1);
	   notHospitalNurses2.add(testNurse4);
	   
	   notHospitalNurses3.add(testNurse3);
	   notHospitalNurses3.add(testNurse2);
	   notHospitalNurses3.add(testNurse5);
	   notHospitalNurses3.add(testNurse1);
	   notHospitalNurses3.add(testNurse4);
	   
	   //return mock nurses from a particular hospital
	   when(hospitalService.getNurses((long)1)).thenReturn(hospitalNurses1);
	   when(hospitalService.getNurses((long)2)).thenReturn(hospitalNurses2);
	   when(hospitalService.getNurses((long)3)).thenReturn(hospitalNurses3);
	   
	   hialeahBloodDrive.add(bloodDrive2);
	   
	   sweetwaterBloodDrive.add(bloodDrive1);
	   
	   coralGablesBloodDrive.add(bloodDrive3);
	   
	   //returns mock blood drive based off location
	   when(bloodDriveService.getBloodDrivesByLocation("Hialeah", "Florida")).thenReturn(hialeahBloodDrive);
	   when(bloodDriveService.getBloodDrivesByLocation("SweetWater", "Florida")).thenReturn(sweetwaterBloodDrive);
	   when(bloodDriveService.getBloodDrivesByLocation("Coral Gables", "Florida")).thenReturn(coralGablesBloodDrive);
	   
	   List<BloodDrive> bloodDrives = new ArrayList<>();
	   bloodDrives.add(bloodDrive1);
	   bloodDrives.add(bloodDrive2);
	   bloodDrives.add(bloodDrive3);
	   
	   //return all blood drives
//	   when(bloodDriveService.getBloodDrives()).thenReturn(bloodDrives);
	   
	   //returns mock blood drive by Id
	   when(bloodDriveService.getBloodDriveById((long)10)).thenReturn(bloodDrive1);
	   when(bloodDriveService.getBloodDriveById((long)11)).thenReturn(bloodDrive2);
	   when(bloodDriveService.getBloodDriveById((long)12)).thenReturn(bloodDrive3);
	   
	   //returns mock employee based off Id
	   when(userService.getEmployeeByUserId((long)1)).thenReturn(testCoordinator1);
	   when(userService.getEmployeeByUserId((long)2)).thenReturn(testCoordinator2);
	   when(userService.getEmployeeByUserId((long)3)).thenReturn(testCoordinator3);
	   when(userService.getEmployeeByUserId((long)4)).thenReturn(testNurse1);
	   when(userService.getEmployeeByUserId((long)5)).thenReturn(testNurse2);
	   when(userService.getEmployeeByUserId((long)6)).thenReturn(testNurse3);
	   when(userService.getEmployeeByUserId((long)7)).thenReturn(testNurse4);
	   when(userService.getEmployeeByUserId((long)8)).thenReturn(testNurse5);
	   when(userService.getEmployeeByUserId((long)9)).thenReturn(testNurse6);
	   when(userService.getEmployeeByUserId((long)13)).thenReturn(testManager1);
	   
	   //returns mock blood drives by coordinator and hospital assigned to blood drive
	   when(bloodDriveService.getBloodDrivesForCoordinator(hospital1, user1)).thenReturn(sweetwaterBloodDrive);
	   when(bloodDriveService.getBloodDrivesForCoordinator(hospital2, user2)).thenReturn(hialeahBloodDrive);
	   when(bloodDriveService.getBloodDrivesForCoordinator(hospital3, user3)).thenReturn(coralGablesBloodDrive);
	   
	   //returns mock blood drives for nurse and hospital assigned to blood drive
	   when(bloodDriveService.getBloodDrivesForNurse(hospital1, user4)).thenReturn(sweetwaterBloodDrive);
	   when(bloodDriveService.getBloodDrivesForNurse(hospital1, user7)).thenReturn(sweetwaterBloodDrive);
	   when(bloodDriveService.getBloodDrivesForNurse(hospital2, user5)).thenReturn(hialeahBloodDrive);
	   when(bloodDriveService.getBloodDrivesForNurse(hospital2, user6)).thenReturn(hialeahBloodDrive);
	   when(bloodDriveService.getBloodDrivesForNurse(hospital2, user8)).thenReturn(hialeahBloodDrive);
	   when(bloodDriveService.getBloodDrivesForNurse(hospital3, user9)).thenReturn(coralGablesBloodDrive);
	   
	   //returns mock blood drive by nurse
	   when(bloodDriveService.getBloodDriveByNurse(10, user4)).thenReturn(bloodDrive1);
	   when(bloodDriveService.getBloodDriveByNurse(10, user7)).thenReturn(bloodDrive1);
	   when(bloodDriveService.getBloodDriveByNurse(11, user5)).thenReturn(bloodDrive2);
	   when(bloodDriveService.getBloodDriveByNurse(11, user6)).thenReturn(bloodDrive2);
	   when(bloodDriveService.getBloodDriveByNurse(11, user8)).thenReturn(bloodDrive2);
	   when(bloodDriveService.getBloodDriveByNurse(12, user9)).thenReturn(bloodDrive3);
	   
	   //returns coordinator for a blood drive
	   when(bloodDriveService.getCoordinator(bloodDrive1)).thenReturn(testCoordinator1);
	   when(bloodDriveService.getCoordinator(bloodDrive2)).thenReturn(testCoordinator2);
	   when(bloodDriveService.getCoordinator(bloodDrive3)).thenReturn(testCoordinator3);
	   
	   //returns blood drive by coordinator
	   when(bloodDriveService.getBloodDriveByCoordinator((long)10, user1)).thenReturn(bloodDrive1);
	   when(bloodDriveService.getBloodDriveByCoordinator((long)11, user2)).thenReturn(bloodDrive2);
	   when(bloodDriveService.getBloodDriveByCoordinator((long)12, user3)).thenReturn(bloodDrive3);
	   
	   //returns set of nurse assigned to a blood drive
	   when(bloodDriveService.getNursesForBloodDrive((long)10, user1)).thenReturn(hospitalNurses1);
	   when(bloodDriveService.getNursesForBloodDrive((long)11, user2)).thenReturn(hospitalNurses2);
	   when(bloodDriveService.getNursesForBloodDrive((long)12, user3)).thenReturn(hospitalNurses3);
	   
	 //returns set of nurses not assigned to a blood drive
	   when(bloodDriveService.getUnassignedNurses((long)10, user1)).thenReturn(notHospitalNurses1);
	   when(bloodDriveService.getUnassignedNurses((long)11, user2)).thenReturn(notHospitalNurses2);
	   when(bloodDriveService.getUnassignedNurses((long)12, user3)).thenReturn(notHospitalNurses3);
	   
	   //returns employees assigned to a hospital
	   when(employeeService.getEmployees(user13, hospital1)).thenReturn(hospitalNurses1);
	   
	   List<ViewModel> vmlist = new ArrayList<>();
	   EmployeeSummaryViewModel esvm1 = new EmployeeSummaryViewModel();
	   esvm1.userId = testNurse1.getUserId();
	   EmployeeSummaryViewModel esvm2 = new EmployeeSummaryViewModel();
	   esvm2.userId = testNurse4.getUserId();
	   vmlist.add(esvm1);
	   vmlist.add(esvm2);
	   
	   //returns a View Model of hospital nurses at hospital 1
	   when(employeeSummaryViewStrategy.CreateViewModel(hospitalNurses1)).thenReturn(vmlist);
	   
	   List<UserNotification> unlist = new ArrayList<>();
	   UserNotification userNotification1 = new UserNotification();
	   userNotification1.setNotification(notification1);
	   unlist.add(userNotification1);
	   
	   //returns a set of user notifications for get user notification
	   when(notificationService.getUserNotifications(user10)).thenReturn(unlist);
	   
	   //returns a set of user notifications for get user notification
	   when(notificationService.getUserNotifications(user10, (long)1)).thenReturn(unlist);
	   
	   
   }

}
