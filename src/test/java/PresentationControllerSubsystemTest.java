import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

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
	@Test
	public void test01S_createHospital() {
		
		
        // Assert.
        assertEquals("User succesfully created! (id = 0)", hc.createHospital("Test Hospital"));;
	}
	
	@Test
	public void test02S_getHospital() {
		
        // Assert.
        assertEquals("Returned hospital succesfully! (name = MDC Hospital)", hc.getHospital(2));;
	}
	
	@Test
	public void test03S_getAllHospitals() {
		
        // Assert.
        assertEquals("FIU Hospital\nMDC Hospital\nUM Hospital\n", hc.getHospitals());;
	}
//--------------------USER CONTROLLER ----------------------------	
	@Test
	public void test04S_createEmployee() throws Exception {
		
		when(uc.getUserService().createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", UserRole.NURSE, (long)2)).thenReturn(testNurse1);
        // Assert.
        assertEquals(testNurse1, uc.createEmployee("nurse@nurse.com", "test123", "test", "nurse", "3052573457", "NURSE", (long)2));;
	}
	
	@Test
	public void test05S_deleteUser() {
		
		doNothing().when(uc.getUserService()).deleteUser("Peter@email.com");
        // Assert.
        assertEquals("User succesfully deleted!", uc.deleteUser("Peter@email.com"));
    }
	
	@Test
	public void test06S_updateUser() {
		
		when(uc.getUserService().updateUser(2, "Foo@gmail.com")).thenReturn(user1);
        // Assert.
        assertEquals(user1, uc.updateUser(2, "Foo@gmail.com"));
    }
	
	@Test
	public void test07S_getNurses() {
		
        // Assert.
        assertEquals(3, ((List <Employee>)uc.getNurses(2)).size());
    }
	
	@Test
	public void test08S_getByEmail() {
		
        // Assert.
        assertEquals(user3, uc.getByEmail("employee3@fiu.edu"));
    }
	
	@Test
	public void test09S_getCurrent() {
		
        // Assert.

    }
	
	@Test
	public void test010S_changePassword() {
		
        // Assert.

    }
	
	@Test
	public void test011S_grantRole() {
		
		user9.grantRole(UserRole.DONOR);
		doNothing().when(uc.getUserService()).updateUser(user9);
        // Assert.
        assertEquals("<200 OK,role granted,{}>", uc.grantRole(user9, UserRole.DONOR).toString());
    }
	
	@Test
	public void test011R_grantRole() {

        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.grantRole(null, UserRole.DONOR).toString());
    }
	
	@Test
	public void test012S_revokeRole() {
		
		user6.revokeRole(UserRole.NURSE);
		doNothing().when(uc.getUserService()).updateUser(user6);
        // Assert.
        assertEquals("<200 OK,role revoked,{}>", uc.revokeRole(user6, UserRole.DONOR).toString());
    }
	
	@Test
	public void test012R_revokeRole() {
		
        // Assert.
        assertEquals("<422 Unprocessable Entity,invalid user id,{}>", uc.revokeRole(null, UserRole.DONOR).toString());
    }
//------------------BLOOD DRIVE CONTROLLER -------------------------
	@Test
	public void test013S_getBloodDrivesByLocation(){
		
		assertEquals(1, ((List <BloodDrive>)bdc.getBloodDrivesByLocation("Hialeah", "Florida")).size());
	}
	
	@Test
	public void test014S_getBloodDriveByIdForDonor(){
		
		assertEquals(((BloodDriveSummaryViewModel)(new BloodDriveDetailViewStrategy().CreateViewModel(bloodDrive1))).title, 
				((BloodDriveSummaryViewModel)bdc.getBloodDriveByIdForDonor((long)10)).title);
	}
	
	@Test
	public void test015S_getBloodDrives(){
		
	}
//-------------------EMPLOYEEE CONTROLLER------------------------
	
	
//-------------------NOTIFICATION CONTROLLER---------------------

}
