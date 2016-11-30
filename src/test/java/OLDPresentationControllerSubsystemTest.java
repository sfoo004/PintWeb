
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
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
public class OLDPresentationControllerSubsystemTest extends StubDB {

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
	public void test01S_createHospital() {
		
        // Assert.
        assertEquals("User succesfully created! (id = 0)", hc.createHospital("Test Hospital"));;
	}
	
	/**
	 * Create new hospital with no name
	 */
	@Test
	public void test01R_createHospital() {
		
        // Assert.
        assertEquals("User succesfully created! (id = 0)", hc.createHospital(""));;
	}
	
	/**
	 * Retrieve a hospital that exists
	 */
	@Test
	public void test02S_getHospital() {
		
        // Assert.
        assertEquals("Returned hospital succesfully! (name = MDC Hospital)", hc.getHospital(2));;
	}
	
	/**
	 * Retrieves all hospitals that exists
	 */
	@Test
	public void test03S_getAllHospitals() {
		
        // Assert.
        assertEquals("FIU Hospital\nMDC Hospital\nUM Hospital\n", hc.getHospitals());;
	}
//--------------------USER CONTROLLER ----------------------------	
	/**
	 * Creates a new employee with correct parameters
	 * @throws Exception
	 */
	@Test
	public void test04S_createEmployee() throws Exception {
		
		when(userService.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenReturn(testNurse1);
        // Assert.
        assertEquals(testNurse1, uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2));;
	}
	
	/**
	 * Deletes an Employee
	 */
	@Test
	public void test05S_deleteUser() {
		
		doNothing().when(userService).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("User succesfully deleted!", uc.deleteUser("Peter@email.com"));
    }
	
	/**
	 * Updates an employee
	 */
	@Test
	public void test06S_updateUser() {
		
		when(userService.updateUser(2, "Foo@gmail.com")).thenReturn(user1);
        // Assert.
        assertEquals(user1, uc.updateUser(2, "Foo@gmail.com"));
    }
	
	/**
	 * get all nurses
	 */
	@Test
	public void test07S_getNurses() {
		
        // Assert.
        assertEquals(3, ((List <Employee>)uc.getNurses(2)).size());
    }
	
	/**
	 * get user by their email
	 */
	@Test
	public void test08S_getByEmail() {
		
        // Assert.
        assertEquals(user3, uc.getByEmail("employee3@fiu.edu"));
    }
	
	/**
	 * Grant role with a valid user
	 */
	@Test
	public void test011S_grantRole() {
		
		doNothing().when(userService).updateUser(user9);
        // Assert.
        assertEquals("<200 OK,role granted,{}>", uc.grantRole(user9, UserRole.DONOR).toString());
    }
	
	/**
	 * Grant role with a invalid user
	 */
	@Test
	public void test011R_grantRole() {

        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.grantRole(null, UserRole.DONOR).toString());
    }
	
	/**
	 * Revoke role with a valid user
	 */
	@Test
	public void test012S_revokeRole() {
		
		doNothing().when(userService).updateUser(user6);
        // Assert.
        assertEquals("<200 OK,role revoked,{}>", uc.revokeRole(user6, UserRole.NURSE).toString());
    }
	
	/**
	 * Revoke role with a valid user
	 */
	@Test
	public void test012R_revokeRole() {
		
        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.revokeRole(null, UserRole.DONOR).toString());
    }
//------------------BLOOD DRIVE CONTROLLER -------------------------
	/**
	 * retrieve blood drives in a set location
	 */
	@Test
	public void test013S_getBloodDrivesByLocation(){
		
		// Assert.
		assertEquals(1, ((List <BloodDrive>)bdc.getBloodDrivesByLocation("Hialeah", "Florida")).size());
	}
	
	/**
	 * Retrieve blood drive in a non stub location
	 */
	@Test
	public void test013R_getBloodDrivesByLocation(){
		
		// Assert.
		assertEquals(0, ((List <BloodDrive>)bdc.getBloodDrivesByLocation("Orlando", "Flroida")).size());
	}
	
	/**
	 * Retrieve blood drive based off ID
	 */
	@Test
	public void test014S_getBloodDriveByIdForDonor(){
		
		// Assert.
		assertEquals(((BloodDriveSummaryViewModel)(new BloodDriveDetailViewStrategy().CreateViewModel(bloodDrive1))).title, 
				((BloodDriveSummaryViewModel)bdc.getBloodDriveByIdForDonor((long)10)).title);
	}
	
	/**
	 * Retrieve blood drive that doesn't exists
	 */
	@Test
	public void test014R_getBloodDriveByIdForDonor(){
		
		// Assert.
		assertEquals(null, 
				((BloodDriveSummaryViewModel)bdc.getBloodDriveByIdForDonor((long)15)).title);
	}
	
	/**
	 * retrieve blood drives when you're a coordinator
	 * @throws Exception
	 */
	@Test
	public void test015S_getBloodDrives() throws Exception{
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
	public void test015R_getBloodDrives() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user5);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDrives().toString());
		
	}
	
	/**
	 * Get blood drives a nurse is in charge of
	 * @throws Exception
	 */
	@Test
	public void test016S_getBloodDrivesForNurse() throws Exception{
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
	public void test016R_getBloodDrivesForNurse() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDrivesForNurse().toString());
		
	}
	
	/**
	 * Get blood drives for a coordinator
	 * @throws Exception
	 */
	@Test
	public void test017S_getBloodDriveByIdForCoordinator() throws Exception{
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
	public void test017R_getBloodDriveByIdForCoordinator() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user7);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDriveByIdForCoordinator((long)10).toString());
		
	}
	
	/**
	 * Get blood drive by ID for a Nurse
	 * @throws Exception
	 */
	@Test
	public void test018S_getBloodDriveByIdForNurse() throws Exception{
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
	public void test018R_getBloodDriveByIdForNurse() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.getBloodDriveByIdForNurse((long)10).toString());
		
	}
	
	/**
	 * Input donor's information if you're a nurse
	 * @throws Exception
	 */
	@Test
	public void test019S_inputDonor() throws Exception{
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
	public void test019R_inputDonor() throws Exception{
		//session belong to this current user
		when(session.getUser()).thenReturn(user2);
		//don't execute method when its called
		doNothing().when(bloodDriveService).inputDonor(user4, (long)1, "hello@world.com");
		// Assert.
		assertEquals("<401 Unauthorized,{}>", bdc.inputDonor((long)1, "hello@world.com").toString());
		
	}
	
	/**
	 * Assign nurses to a blood drive if you're a coordiantor
	 * @throws Exception
	 */
	@Test
	public void test020S_assignNurses() throws Exception{
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
	public void test020R_assignNurses() throws Exception{
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
	 * unassign nurses to a blood drive if you're a coordinator
	 * @throws Exception
	 */
	@Test
	public void test021S_unassignNurses() throws Exception{
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
	public void test021R_unassignNurses() throws Exception{
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
//-------------------EMPLOYEEE CONTROLLER------------------------
	
	/**
	 * get Employees if your manager
	 * @throws Exception
	 */
	@Test
	public void test022S_getEmployees() throws Exception{
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
	public void test022R_getEmployees() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>" , ec.getEmployees().toString());
		
	}
	
	/**
	 * Create a new employee if your a manager
	 * @throws Exception
	 */
	@Test
	public void test023S_createEmployee() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		//returns testNurse6 when method is called
		when(userService.createEmployee(testNurse6.getEmail(), testNurse6.getPassword(), testNurse6.getFirstName(),
				testNurse6.getLastName(), testNurse6.getPhoneNumber(), UserRole.NURSE, testNurse6.getHospitalId().getId())).thenReturn(testNurse6);
		
		// Assert.
		assertEquals("<200 OK,{}>", ec.createEmployee(testNurse6).toString());
		
	}
	
	/**
	 * Create a new employee if your a not manager
	 * @throws Exception
	 */
	@Test
	public void test023R_createEmployee() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		//returns test nurse 6
		when(userService.createEmployee(testNurse6.getEmail(), testNurse6.getPassword(), testNurse6.getFirstName(),
				testNurse6.getLastName(), testNurse6.getPhoneNumber(), UserRole.NURSE, testNurse6.getHospitalId().getId())).thenReturn(testNurse6);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", ec.createEmployee(testNurse6).toString());
		
	}
//-------------------NOTIFICATION CONTROLLER---------------------
	/**
	 * get notifications if you're a donor
	 * @throws Exception
	 */
	@Test
	public void test024S_getUserNotification() throws Exception{
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
	public void test024R_getUserNotification() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user13);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", n.getUserNotification().toString());
		
	}
	
	/**
	 * get notifications if you're a donor
	 * @throws Exception
	 */
	@Test
	public void test025S_getUserNotifications() throws Exception{
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
	public void test025R_getUserNotifications() throws Exception{
		//session belong to this current user
		when(s.getUser()).thenReturn(user1);
		
		// Assert.
		assertEquals("<401 Unauthorized,{}>", n.getUserNotifications((long)1).toString());
		
	}
}
	
