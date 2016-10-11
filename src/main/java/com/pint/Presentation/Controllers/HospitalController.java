package com.pint.Presentation.Controllers;

import org.mockito.Mockito;
import com.pint.Data.Models.Hospital;
import com.pint.Data.Repositories.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HospitalController {
	/**
	 * MOCK Hospital
	 * MOCK hospitalRepository
	 * @param hospitalName
	 * @return
	 */
    @RequestMapping("/createhospital")
    @ResponseBody
    public String createHospital(@RequestParam(value = "name", defaultValue = "test_hosptial") String hospitalName) {
        Hospital hospital = null;
        try {
            hospital = new Hospital(hospitalName);
            hospitalRepository.create(hospital);
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }

        return "User succesfully created! (id = " + hospital.getId() + ")";
    }

    /**
     * MOCK Hospital
	 * MOCK hospitalRepository
     * @param hospitalId
     * @return
     */
    @RequestMapping("hospital")
    @ResponseBody
    public String getHospital(@RequestParam(value = "id", defaultValue = "1") long hospitalId) {
        Hospital hospital = null;
        try {
            hospital = hospitalRepository.get(hospitalId);
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }

        return "Returned hospital succesfully! (name = " + hospital.getName() + ")";
    }

    /**
     * MOCK Hospital
	 * MOCK hospitalRepository
     * @return
     */
    @RequestMapping("/hospitals")
    @ResponseBody
    public String getHospitals() {
        String hospitals = "";
        List<Hospital> hospitalList = null;

        try {
            hospitalList = hospitalRepository.getHospitals();
            for (int i = 0; i < hospitalList.size(); i++) {
                hospitals += hospitalList.get(i).getName() + "\n";
            }
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }

        return hospitals;
    }

    @Autowired
    private HospitalRepository hospitalRepository;
}

