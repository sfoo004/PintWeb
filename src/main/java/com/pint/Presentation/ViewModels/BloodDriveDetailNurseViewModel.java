package com.pint.Presentation.ViewModels;

import java.sql.Date;

public class BloodDriveDetailNurseViewModel extends BloodDriveSummaryViewModel {

    public int numberOfDonors;

    public String hospitalName;

    public ViewModel coordinator;

    public BloodDriveDetailNurseViewModel() {
    }

    public BloodDriveDetailNurseViewModel(long bloodDriveId,
                                          String title,
                                          String description,
                                          Date startTime,
                                          Date endTime,
                                          String hospitalName,
                                          String address,
                                          int numberOfDonors,
                                          ViewModel coordinatorViewModel) {
        super(bloodDriveId, title, description, startTime, endTime, address);
        this.numberOfDonors = numberOfDonors;
        this.coordinator = coordinatorViewModel;
        this.hospitalName = hospitalName;
    }
}
