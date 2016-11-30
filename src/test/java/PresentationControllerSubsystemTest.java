import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Validators.ValidationException;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import com.pint.Data.Models.UserNotification;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Presentation.Controllers.*;
import com.pint.Presentation.ViewModels.BloodDriveDetailCoordinatorViewModel;
import com.pint.Presentation.ViewModels.BloodDriveDetailNurseViewModel;
import com.pint.Presentation.ViewModels.BloodDriveSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;
import com.pint.Presentation.ViewStrategies.BloodDriveDetailViewStrategy;
import com.pint.Presentation.ViewStrategies.BloodDriveSummaryViewStrategy;
import com.pint.Presentation.ViewStrategies.EmployeeSummaryViewStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@RunWith(MockitoJUnitRunner.class)
public class PresentationControllerSubsystemTest extends StubDB {

	@Before
	public void setUp(){
		createStubDB();
	}
	
	@After
	public void tearDown(){
		
	}
//------------------------ HOSPITAL CONTROLLER ----------------------------
	/**
	 * Create new hospital with correct input
	 */
	@Test
	public void SSTW010_createHospital_SD() {
		
        // Assert.
        assertEquals("User succesfully created! (id = 0)", hc.createHospital("Test Hospital"));;
	}
	
	/**
	 * Create new hospital with no name
	 */
	@Test
	public void SSTW011_createHospital_RD() {
		
        // Assert.
        assertEquals("User succesfully created! (id = 0)", hc.createHospital(""));;
	}
	
	/**
	 * Retrieve a hospital that exists
	 */
	@Test
	public void SSTW020_getHospital_SD() {
		
        // Assert.
        assertEquals("Returned hospital succesfully! (name = MDC Hospital)", hc.getHospital(2));;
	}
	
	/**
	 * Retrieve a hospital that exists, but an exception is thrown
	 */
	@Test
	public void SSTW021_getHospital_RD() {
		
		when(hospitalRepository.get(0)).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error creating the user: java.lang.Exception", hc.getHospital(0));;
	}
	
	/**
	 * Retrieves all hospitals that exists
	 */
	@Test
	public void SSTW030_getAllHospitals_SD() {
		
        // Assert.
        assertEquals("FIU Hospital\nMDC Hospital\nUM Hospital\n", hc.getHospitals());;
	}
	
	/**
	 * Retrieves all hospitals that exists, but an exception is thrown
	 */
	@Test
	public void SSTW031_getAllHospitals_RD() {
		
		when(hospitalRepository.getHospitals()).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error creating the user: java.lang.Exception", hc.getHospitals());;
	}
//--------------------USER CONTROLLER ----------------------------	
	/**
	 * Creates a new employee with correct parameters
	 * @throws Exception
	 */
	@Test
	public void SSTW040_createEmployee_SD() throws Exception {
		
		when(userService.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenReturn(testNurse1);
        // Assert.
        assertEquals(testNurse1, uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2));;
	}
	
	/**
	 * Creates a new employee, but throws an exception
	 * @throws Exception
	 */
	@Test
	public void SSTW041_createEmployee_RD() throws Exception {
		
		when(userService.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error creating employee: java.lang.Exception", uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2).toString());;
	}
	
	/**
	 * Deletes an Employee
	 */
	@Test
	public void SSTW050_deleteUser_SD() {
		
		doNothing().when(userService).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("User succesfully deleted!", uc.deleteUser("Peter@email.com"));
    }
	
	/**
	 * Deletes an Employee, but throws an exception
	 */
	@Test
	public void SSTW051_deleteUser_RD() {
		
		doThrow(Exception.class).when(userService).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("Error deleting the user:java.lang.Exception", uc.deleteUser("Peter@email.com"));
    }
	
	/**
	 * Updates an employee
	 */
	@Test
	public void SSTW060_updateUser_SD() {
		
		when(userService.updateUser(2, "Foo@gmail.com")).thenReturn(user1);
        // Assert.
        assertEquals(user1, uc.updateUser(2, "Foo@gmail.com"));
    }
	
	/**
	 * Updates an employee, but an exception is thrown
	 */
	@Test
	public void SSTW061_updateUser_RD() {
		
		when(userService.updateUser(2, "Foo@gmail.com")).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error updating the user: java.lang.Exception", uc.updateUser(2, "Foo@gmail.com"));
    }
	
	/**
	 * get all nurses
	 */
	@Test
	public void SSTW070_getNurses_SD() {
		
        // Assert.
        assertEquals(3, ((List <Employee>)uc.getNurses(2)).size());
    }
	
	/**
	 * get all nurses, but an exception is thrown
	 */
	@Test
	public void SSTW071_getNurses_RD() {
		
		when(hospitalService.getNurses((long)2)).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error getting nurses\n", uc.getNurses(2).toString());
    }
	
	/**
	 * get user by their email
	 */
	@Test
	public void SSTW080_getByEmail_SD() {
		
        // Assert.
        assertEquals(user3, uc.getByEmail("employee3@fiu.edu"));
    }
	
	/**
	 * get user by their email, but an exception is thrown
	 */
	@Test
	public void SSTW081_getByEmail_RD() {
		
		when(userService.getUserByEmail("employee3@fiu.edu")).thenThrow(Exception.class);
        // Assert.
        assertEquals("User not found", uc.getByEmail("employee3@fiu.edu"));
    }
	
	/**
	 * get current user
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW090_getCurrent_SD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals(testNurse2,uc.getCurrent());
	}
	
	/**
	 * get current user but they're a donor(Not Employee)
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW091_getCurrent_RD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated10);
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals(user10.getUsername(),((User)uc.getCurrent()).getUsername());
	}
	
	/**
	 * get current user but they are not authenticated
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW092_getCurrent_RD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals((new User(null)).getUsername(),((User)uc.getCurrent()).getUsername());
	}
	
	/**
	 * Change password
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW100_changePassword_SD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);
		//set up pw encryption
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		//set password to user 5
		user5.setPassword(pwEncoder.encode("oldpass12345"));
		//create test user 5 and set old password and new password
		User testUser5 = new User("test@test.com");
		testUser5.setNewPassword("pass12345");
		testUser5.setPassword("oldpass12345");
		//Assert.
		assertEquals("<200 OK,password changed,{}>",uc.changePassword(testUser5).toString());
	}
	
	/**
	 * Change password, but old password doesn't match
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW101_changePassword_RD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);
		//set up pw encryption
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		//set password to user 5
		user5.setPassword(pwEncoder.encode("oldpass12345"));
		//create test user 5 and set old password and new password
		User testUser5 = new User("test@test.com");
		testUser5.setNewPassword("pass12345");
		testUser5.setPassword("wrongpass");
		//Assert.
		assertEquals("<422 Unprocessable Entity,old password mismatch,{}>",uc.changePassword(testUser5).toString());
	}
	
	/**
	 * Change password, but password is too short
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW102_changePassword_RD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);

		user5.setNewPassword("abc");
		//Assert.
		assertEquals("<422 Unprocessable Entity,new password to short,{}>",uc.changePassword(user5).toString());
	}
	
	/**
	 * Change password, but password is null
	 * @throws InterruptedException
	 */
	@Test
	public void SSTW103_changePassword_RD() throws InterruptedException{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);

		user5.setNewPassword(null);
		//Assert.
		assertEquals("<422 Unprocessable Entity,new password to short,{}>",uc.changePassword(user5).toString());
	}
	
	/**
	 * Grant role with a valid user
	 */
	@Test
	public void SSTW110_grantRole_SD() {
		
		doNothing().when(userService).updateUser(user9);
        // Assert.
        assertEquals("<200 OK,role granted,{}>", uc.grantRole(user9, UserRole.DONOR).toString());
    }
	
	/**
	 * Grant role with a invalid user
	 */
	@Test
	public void SSTW111_grantRole_RD() {

        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.grantRole(null, UserRole.DONOR).toString());
    }
	
	/**
	 * Revoke role with a valid user
	 */
	@Test
	public void SSTW120_revokeRole_SD() {
		
		doNothing().when(userService).updateUser(user6);
        // Assert.
        assertEquals("<200 OK,role revoked,{}>", uc.revokeRole(user6, UserRole.NURSE).toString());
    }
	
	/**
	 * Revoke role with a invalid user
	 */
	@Test
	public void SSTW121_revokeRole_RD() {
		
        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.revokeRole(null, UserRole.DONOR).toString());
    }
	
	/**
	 * Get list of all current users
	 */
	@Test
	public void SSTW130_list_SD() {
		
        // Assert.
        assertEquals(13, uc.list().size());
    }
//------------------BLOOD DRIVE CONTROLLER -------------------------
	/**
	 * retrieve blood drives in a set location
	 */
	@Test
	public void SSTW140_getBloodDrivesByLocation_SD(){
		
		// Assert.
		assertEquals(1, ((List <BloodDrive>)bdc.getBloodDrivesByLocation("Hialeah", "Florida")).size());
	}
	
	/**
	 * Retrieve blood drive in a non stub location
	 */
	@Test
	public void SSTW141_getBloodDrivesByLocation_RD(){
		
		// Assert.
		assertEquals(0, ((List <BloodDrive>)bdc.getBloodDrivesByLocation("Orlando", "Florida")).size());
	}
	
	/**
	 * Retrieve blood drive in a non stub location, gets exception thrown
	 */
	@Test
	public void SSTW142_getBloodDrivesByLocation_RD(){
		
		when(bloodDriveService.getBloodDrivesByLocation("Orlando", "Florida")).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDrivesByLocation("Orlando", "Florida").toString());
	}
	
	/**
	 * Retrieve blood drive based off ID
	 */
	@Test
	public void SSTW150_getBloodDriveByIdForDonor_SD(){
		
		// Assert.
		assertEquals(((BloodDriveSummaryViewModel)(new BloodDriveDetailViewStrategy().CreateViewModel(bloodDrive1))).title, 
				((BloodDriveSummaryViewModel)bdc.getBloodDriveByIdForDonor((long)10)).title);
	}
	
	/**
	 * Retrieve blood drive that doesn't exists
	 */
	@Test
	public void SSTW151_getBloodDriveByIdForDonor_RD(){
		
		// Assert.
		assertEquals(null, 
				((BloodDriveSummaryViewModel)bdc.getBloodDriveByIdForDonor((long)15)).title);
	}
	
	/**
	 * Retrieve blood drive that doesn't exists, but throws an Exception
	 */
	@Test
	public void SSTW152_getBloodDriveByIdForDonor_RD(){
		
		when(bloodDriveService.getBloodDriveById((long)15)).thenThrow(Exception.class);
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDriveByIdForDonor((long)15).toString());
	}
	
	/**
	 * retrieve blood drives when you're a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW160_getBloodDrives_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user1);
		
		// Assert.
		assertEquals(((List<ViewModel>)new BloodDriveSummaryViewStrategy().CreateViewModel(sweetwaterBloodDrive)).size(), ((List<ViewModel>)bdc.getBloodDrives()).size());
		
	}
	
	/**
	 * Retrieve blood drives if you're not a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW161_getBloodDrives_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user5);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDrives().toString());
		
	}
	
	/**
	 * Retrieve blood drives if you're not a coordinator, but throws an Exception
	 * @throws Exception
	 */
	@Test
	public void SSTW162_getBloodDrives_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDrives().toString());
		
	}
	
	/**
	 * Get blood drives a nurse is in charge of
	 * @throws Exception
	 */
	@Test
	public void SSTW170_getBloodDrivesForNurse_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user4);
		
		// Assert.
		assertEquals(((List<ViewModel>)new BloodDriveSummaryViewStrategy().CreateViewModel(sweetwaterBloodDrive)).size(), ((List<ViewModel>)bdc.getBloodDrivesForNurse()).size());
		
	}
	
	/**
	 * Get blood drives a for someone thats not a nurse
	 * @throws Exception
	 */
	@Test
	public void SSTW171_getBloodDrivesForNurse_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDrivesForNurse().toString());
		
	}
	
	/**
	 * Get blood drives a for someone thats not a nurse, but an exception gets thrown
	 * @throws Exception
	 */
	@Test
	public void SSTW172_getBloodDrivesForNurse_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDrivesForNurse().toString());
		
	}
	
	/**
	 * Get blood drives for a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW180_getBloodDriveByIdForCoordinator_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user1);
		// Assert.
		assertEquals(((BloodDriveDetailCoordinatorViewModel)new BloodDriveDetailViewStrategy(hospitalNurses1, notHospitalNurses1).CreateViewModel(bloodDrive1)).title, 
				((BloodDriveDetailCoordinatorViewModel)bdc.getBloodDriveByIdForCoordinator((long)10)).title);
		
	}
	
	/**
	 * Get blood drives for someone not a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW181_getBloodDriveByIdForCoordinator_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user7);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDriveByIdForCoordinator((long)10).toString());
		
	}
	
	/**
	 * Get blood drives for someone not a coordinator, but an exception get thrown
	 * @throws Exception
	 */
	@Test
	public void SSTW182_getBloodDriveByIdForCoordinator_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDriveByIdForCoordinator((long)10).toString());
		
	}
	
	/**
	 * Get blood drive by ID for a Nurse
	 * @throws Exception
	 */
	@Test
	public void SSTW190_getBloodDriveByIdForNurse_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user4);
		// Assert.
		assertEquals(((BloodDriveDetailNurseViewModel)new BloodDriveDetailViewStrategy(
                new EmployeeSummaryViewStrategy().CreateViewModel(testCoordinator1))
                .CreateViewModel(bloodDrive1)).title, 
				((BloodDriveDetailNurseViewModel)bdc.getBloodDriveByIdForNurse((long)10)).title);
		
	}
	
	/**
	 * Get blood drive by ID for someone not a nurse
	 * @throws Exception
	 */
	@Test
	public void SSTW191_getBloodDriveByIdForNurse_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDriveByIdForNurse((long)10).toString());
		
	}
	
	/**
	 * Get blood drive by ID for someone not a nurse, but an exception is thrown
	 * @throws Exception
	 */
	@Test
	public void SSTW192_getBloodDriveByIdForNurse_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.getBloodDriveByIdForNurse((long)10).toString());
		
	}
	
	/**
	 * Input donor's information if you're a nurse
	 * @throws Exception
	 */
	@Test
	public void SSTW200_inputDonor_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user4);
		//Don't execute method when its called
		doNothing().when(bloodDriveService).inputDonor(user4, (long)1, "hello@world.com");
		// Assert.
		assertEquals((long)1, bdc.inputDonor((long)1, "hello@world.com"));
		
	}
	
	/**
	 * Input donor's information if you're a not nurse
	 * @throws Exception
	 */
	@Test
	public void SSTW201_inputDonor_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		//don't execute method when its called
		doNothing().when(bloodDriveService).inputDonor(user4, (long)1, "hello@world.com");
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.inputDonor((long)1, "hello@world.com").toString());
		
	}
	
	/**
	 * Input donor's information if you're a not nurse, throws an Validation Exception
	 * @throws Exception
	 */
	@Test
	public void SSTW202_inputDonor_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(ValidationException.class);

		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.inputDonor((long)1, "hello@world.com").toString());
		
	}
	
	/**
	 * Input donor's information if you're a nurse, throws an Exception
	 * @throws Exception
	 */
	@Test
	public void SSTW203_inputDonor_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user4);
		//don't execute method when its called
		doThrow(Exception.class).when(bloodDriveService).inputDonor(user4, (long)1, "hello@world.com");

		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.inputDonor((long)1, "hello@world.com").toString());
		
	}
	
	/**
	 * Assign nurses to a blood drive if you're a coordiantor
	 * @throws Exception
	 */
	@Test
	public void SSTW210_assignNurses_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user1);
		//list holds employees
		List <Long> nurse = new ArrayList<>();
		nurse.add((long)5);
		nurse.add((long)6);
		//list that hold employee ID
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		//don't execute method when its called
		doNothing().when(bloodDriveService).assignNurses(user1, (long)1, nurse);
		// Assert.
		assertEquals((long)1, bdc.assignNurses((long) 1, nurseInt));
		
	}
	
	/**
	 * Assign nurses to a blood drive if you're a not coordiantor
	 * @throws Exception
	 */
	@Test
	public void SSTW211_assignNurses_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user5);
		//list holds employees
		List <Long> nurse = new ArrayList<>();
		nurse.add((long)5);
		nurse.add((long)6);
		//list holds employee ID
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		//don't execute method when its called
		doNothing().when(bloodDriveService).assignNurses(user1, (long)1, nurse);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.assignNurses((long) 1, nurseInt).toString());
		
	}
	
	/**
	 * Assign nurses to a blood drive if you're a not coordiantor, but throws an exception
	 * @throws Exception
	 */
	@Test
	public void SSTW212_assignNurses_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		//list holds employee ID
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.assignNurses((long) 1, nurseInt).toString());
		
	}
	
	/**
	 * unassign nurses to a blood drive if you're a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW220_unassignNurses_SD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user1);
		//list holds employee
		List <Long> nurse = new ArrayList<>();
		nurse.add((long)5);
		nurse.add((long)6);
		//list holds employee ID
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		//don't execute method when its called
		doNothing().when(bloodDriveService).unassignNurses(user1, (long)1, nurse);
		// Assert.
		assertEquals((long)1, bdc.unassignNurses((long) 1, nurseInt));
		
	}
	
	/**
	 * unassign nurses to a blood drive if you're a not a coordinator
	 * @throws Exception
	 */
	@Test
	public void SSTW221_unassignNurses_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user6);
		List <Long> nurse = new ArrayList<>();
		nurse.add((long)5);
		nurse.add((long)6);
		
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		//don't execute method when its called
		doNothing().when(bloodDriveService).unassignNurses(user1, (long)1, nurse);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.unassignNurses((long) 1, nurseInt).toString());
		
	}
	
	/**
	 * unassign nurses to a blood drive if you're a not a coordinator, but throws an exception
	 * @throws Exception
	 */
	@Test
	public void SSTW222_unassignNurses_RD() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenThrow(Exception.class);
		
		ArrayList <Integer> nurseInt = new ArrayList<>();
		nurseInt.add(5);
		nurseInt.add(6);
		// Assert.
		assertEquals("<400 Bad Request,{}>", bdc.unassignNurses((long) 1, nurseInt).toString());
		
	}
//-------------------EMPLOYEEE CONTROLLER------------------------
	
	/**
	 * get Employees if your manager
	 * @throws Exception
	 */
	@Test
	public void SSTW230_getEmployees_SD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		List<ViewModel> list = (List<ViewModel>)ec.getEmployees();
		
		// Assert.
		assertEquals(2 , list.size());
		
	}
	
	/**
	 * get Employees if your not manager
	 * @throws Exception
	 */
	@Test
	public void SSTW231_getEmployees_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>" , ec.getEmployees().toString());
		
	}
	
	/**
	 * get Employees if your a manager, but an exception is thrown
	 * @throws Exception
	 */
	@Test
	public void SSTW232_getEmployees_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		when(userService.getEmployeeByUserId((long)13)).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>" , ec.getEmployees().toString());
		
	}
	
	/**
	 * Create a new employee if you're a manager
	 * @throws Exception
	 */
	@Test
	public void SSTW240_createEmployee_SD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		//returns testNurse6 when method is called
		when(userService.createEmployee(testNurse6.getEmail(), testNurse6.getPassword(), testNurse6.getFirstName(),
				testNurse6.getLastName(), testNurse6.getPhoneNumber(), UserRole.NURSE, testNurse6.getHospitalId().getId())).thenReturn(testNurse6);
		
		// Assert.
		assertEquals("<200 OK,{}>", ec.createEmployee(testNurse6).toString());
		
	}
	
	/**
	 * Create a new employee if you're a not manager
	 * @throws Exception
	 */
	@Test
	public void SSTW241_createEmployee_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		//returns test nurse 6
		when(userService.createEmployee(testNurse6.getEmail(), testNurse6.getPassword(), testNurse6.getFirstName(),
				testNurse6.getLastName(), testNurse6.getPhoneNumber(), UserRole.NURSE, testNurse6.getHospitalId().getId())).thenReturn(testNurse6);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", ec.createEmployee(testNurse6).toString());
		
	}
	
	/**
	 * Create a new employee if you're a manager, but throws an exception
	 * @throws Exception
	 */
	@Test
	public void SSTW242_createEmployee_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		//returns test nurse 6
		when(userService.getEmployeeByUserId((long)13)).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", ec.createEmployee(testNurse6).toString());
		
	}
	
	/**
	 * Create a new employee if you're a manager, but throws an Validation exception
	 * @throws Exception
	 */
	@Test
	public void SSTW243_createEmployee_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		//returns test nurse 6
		when(userService.getEmployeeByUserId((long)13)).thenThrow(ValidationException.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", ec.createEmployee(testNurse6).toString());
		
	}
	
	
//-------------------NOTIFICATION CONTROLLER---------------------
	/**
	 * get notifications if you're a donor
	 * @throws Exception
	 */
	@Test
	public void SSTW250_getUserNotification_SD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user10);
		
		// Assert.
		assertEquals(1, ((List<UserNotification>)n.getUserNotification()).size());
		
	}
	
	/**
	 * get notifications if you're a not donor
	 * @throws Exception
	 */
	@Test
	public void SSTW251_getUserNotification_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", n.getUserNotification().toString());
		
	}
	
	/**
	 * get notifications if you're a donor, but an exception gets thrown when session get user is called
	 * @throws Exception
	 */
	@Test
	public void SSTW252_getUserNotification_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", n.getUserNotification().toString());
		
	}
	
	/**
	 * get notifications if you're a donor
	 * @throws Exception
	 */
	@Test
	public void SSTW260_getUserNotifications_SD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user10);
		
		// Assert.
		assertEquals(1, ((List<UserNotification>)n.getUserNotifications((long)1)).size());
		
	}
	
	/**
	 * get notifications if you're not a donor
	 * @throws Exception
	 */
	@Test
	public void SSTW261_getUserNotifications_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", n.getUserNotifications((long)1).toString());
		
	}
	
	/**
	 * get notifications if you're a donor, but an exception gets thrown when session get user is called
	 * @throws Exception
	 */
	@Test
	public void SSTW262_getUserNotifications_RD() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenThrow(Exception.class);
		
		// Assert.
		assertEquals("<400 Bad Request,{}>", n.getUserNotifications((long)1).toString());
		
	}
	
//-----------------------SESSION CONTROLLER--------------------------------
	
	/**
	 * return user that is authenticated
	 * @throws Exception
	 */
	@Test
	public void SSTW270_getUser_SD() throws Exception{
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		SecurityContextHolder.setContext(securityContext);
		
		// Assert.
		assertEquals(user5.getUsername(), ses.getUser().getUsername());
		
	}
	
//	/**
//	 * return user not authenticated
//	 * @throws Exception
//	 */
//	@Test
//	public void test026R_getUser() throws Exception{
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Authentication authentication = Mockito.mock(Authentication.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		
//		// Assert.
//		assertEquals("<401 Unauthorized,{}>", ses.getUser());
//		
//	}
	
}
