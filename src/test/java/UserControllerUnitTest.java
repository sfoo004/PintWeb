import static org.junit.Assert.*;

import com.pint.BusinessLogic.Security.UserRole;
import com.pint.Data.Models.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import org.mockito.Mock;
import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Data.Models.Hospital;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Presentation.Controllers.*;
import com.pint.Presentation.ViewModels.BloodDriveSummaryViewModel;
import com.pint.Presentation.ViewStrategies.BloodDriveDetailViewStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class UserControllerUnitTest extends StubDB {

	@Before
	public void setUp() throws Exception {
		createStubDB();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Creates a new employee with correct parameters
	 * @throws Exception
	 */
	@Test
	public void UW001_createEmployee_SD() throws Exception {
		
		when(userService.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenReturn(testNurse1);
        // Assert.
        assertEquals(testNurse1, uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2));;
	}
	
	/**
	 * Creates a new employee, but throws an exception
	 * @throws Exception
	 */
	@Test
	public void UW002_createEmployee_RD() throws Exception {
		
		when(userService.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error creating employee: java.lang.Exception", uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2).toString());;
	}
	
	/**
	 * Deletes an Employee
	 */
	@Test
	public void UW003_deleteUser_SD() {
		
		doNothing().when(userService).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("User succesfully deleted!", uc.deleteUser("Peter@email.com"));
    }
	
	/**
	 * Deletes an Employee, but throws an exception
	 */
	@Test
	public void UW004_deleteUser_RD() {
		
		doThrow(Exception.class).when(userService).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("Error deleting the user:java.lang.Exception", uc.deleteUser("Peter@email.com"));
    }
	
	/**
	 * Updates an employee
	 */
	@Test
	public void UW005_updateUser_SD() {
		
		when(userService.updateUser(2, "Foo@gmail.com")).thenReturn(user1);
        // Assert.
        assertEquals(user1, uc.updateUser(2, "Foo@gmail.com"));
    }
	
	/**
	 * Updates an employee, but an exception is thrown
	 */
	@Test
	public void UW006_updateUser_RD() {
		
		when(userService.updateUser(2, "Foo@gmail.com")).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error updating the user: java.lang.Exception", uc.updateUser(2, "Foo@gmail.com"));
    }
	
	/**
	 * get all nurses
	 */
	@Test
	public void UW007_getNurses_SD() {
		
        // Assert.
        assertEquals(3, ((List <Employee>)uc.getNurses(2)).size());
    }
	
	/**
	 * get all nurses, but an exception is thrown
	 */
	@Test
	public void UW008_getNurses_RD() {
		
		when(hospitalService.getNurses((long)2)).thenThrow(Exception.class);
        // Assert.
        assertEquals("Error getting nurses\n", uc.getNurses(2).toString());
    }
	
	/**
	 * get user by their email
	 */
	@Test
	public void UW009_getByEmail_SD() {
		
        // Assert.
        assertEquals(user3, uc.getByEmail("employee3@fiu.edu"));
    }
	
	/**
	 * get user by their email, but an exception is thrown
	 */
	@Test
	public void UW010_getByEmail_RD() {
		
		when(userService.getUserByEmail("employee3@fiu.edu")).thenThrow(Exception.class);
        // Assert.
        assertEquals("User not found", uc.getByEmail("employee3@fiu.edu"));
    }
	
	/**
	 * get current user
	 * @throws InterruptedException
	 */
	@Test
	public void UW011_getCurrent_SD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		//set security context holder to mock
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals(testNurse2,uc.getCurrent());
	}
	
	/**
	 * get current user but they're a donor(Not Employee)
	 * @throws InterruptedException
	 */
	@Test
	public void UW012_getCurrent_RD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated10);
		//set security context holder to mock
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals(user10.getUsername(),((User)uc.getCurrent()).getUsername());
	}
	
	/**
	 * get current user but they are not authenticated
	 * @throws InterruptedException
	 */
	@Test
	public void UW013_getCurrent_RD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		//mock authentication
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		//set security context holder to mock
		SecurityContextHolder.setContext(securityContext);
		//Assert.
		assertEquals((new User(null)).getUsername(),((User)uc.getCurrent()).getUsername());
	}
	
	/**
	 * Change password
	 * @throws InterruptedException
	 */
	@Test
	public void UW014_changePassword_SD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		//set security context holder to mock
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
	public void UW015_changePassword_RD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		//set security context holder to mock
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
	public void UW016_changePassword_RD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		//set security context holder to mock
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
	public void UW017_changePassword_RD() throws InterruptedException{
		//mock security context
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthenticated5);
		//set security context holder to mock
		SecurityContextHolder.setContext(securityContext);

		user5.setNewPassword(null);
		//Assert.
		assertEquals("<422 Unprocessable Entity,new password to short,{}>",uc.changePassword(user5).toString());
	}
	
	/**
	 * Grant role with a valid user
	 */
	@Test
	public void UW018_grantRole_SD() {
		
		doNothing().when(userService).updateUser(user9);
        // Assert.
        assertEquals("<200 OK,role granted,{}>", uc.grantRole(user9, UserRole.DONOR).toString());
    }
	
	/**
	 * Grant role with a invalid user
	 */
	@Test
	public void UW019_grantRole_RD() {

        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.grantRole(null, UserRole.DONOR).toString());
    }
	
	/**
	 * Revoke role with a valid user
	 */
	@Test
	public void UW020_revokeRole_SD() {
		
		doNothing().when(userService).updateUser(user6);
        // Assert.
        assertEquals("<200 OK,role revoked,{}>", uc.revokeRole(user6, UserRole.NURSE).toString());
    }
	
	/**
	 * Revoke role with a invalid user
	 */
	@Test
	public void UW021_revokeRole_RD() {
		
        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.revokeRole(null, UserRole.DONOR).toString());
    }
	
	/**
	 * Get list of all current users
	 */
	@Test
	public void UW022_list_SD() {
		
        // Assert.
        assertEquals(13, uc.list().size());
}
}
