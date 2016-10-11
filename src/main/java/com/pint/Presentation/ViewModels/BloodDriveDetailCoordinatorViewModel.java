package com.pint.Presentation.ViewModels;

import com.pint.Data.Models.Employee;
import com.pint.Presentation.ViewStrategies.BloodDriveDetailViewStrategy;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

/**
 * Created by Dionny on 11/26/2015.
 */
public class BloodDriveDetailCoordinatorViewModel extends BloodDriveSummaryViewModel {

    public int numberOfDonors;
    public List<Employee> assignedNurses;
    public List<Employee> unassignedNurses;

    public BloodDriveDetailCoordinatorViewModel(){
    }

    public BloodDriveDetailCoordinatorViewModel(long bloodDriveId,
                                                String title,
                                                String description,
                                                Date startTime,
                                                Date endTime,
                                                String address,
                                                int numberOfDonors,
                                                List<Employee> assignedNurses,
                                                List<Employee> unassignedNurses)
    {
        super(bloodDriveId, title, description, startTime, endTime, address);
        this.numberOfDonors = numberOfDonors;
        this.assignedNurses = assignedNurses;
        this.unassignedNurses = unassignedNurses;
    }
}

