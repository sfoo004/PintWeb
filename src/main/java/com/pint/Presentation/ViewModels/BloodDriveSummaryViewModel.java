package com.pint.Presentation.ViewModels;

import java.sql.Date;

/**
 * Created by Dionny on 11/26/2015.
 */
public class BloodDriveSummaryViewModel extends ViewModel {
    public long bloodDriveId;

    public String title;

    public String description;

    public Date startTime;

    public Date endTime;

    public String address;

    public BloodDriveSummaryViewModel() {
    }

    public BloodDriveSummaryViewModel(long bloodDriveId, String title, String description, Date startTime, Date endTime, String address) {
        this.bloodDriveId = bloodDriveId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
    }
}
