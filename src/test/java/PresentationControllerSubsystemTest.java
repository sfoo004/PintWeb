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

import com.pint.Data.DataFacade;
import com.pint.Data.Models.Hospital;
import com.pint.Data.Repositories.HospitalRepository;
import com.pint.Presentation.Controllers.*;

import junit.framework.Assert;


@RunWith(MockitoJUnitRunner.class)
public class PresentationControllerSubsystemTest extends StubDB {

	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
		
	}

	@Test
	public void test01() {
		
		HospitalController hc = Mockito.mock(HospitalController.class);
		controllerFacade = new ControllerFacade(hc);
		controllerFacade.createHospital("Test Hospital");

        // Assert.
        verify(hc).createHospital("Test Hospital");
	}
	
	@Test
	public void test02() {
		
        // Act.
		HospitalRepository hr = Mockito.mock(HospitalRepository.class);
		controllerFacade = new ControllerFacade(hr);
		createStubDB();
        List<Hospital> hospitals = controllerFacade.getHospitals();

        // Assert.
        assertEquals(3, hospitals.size());
	}

}
