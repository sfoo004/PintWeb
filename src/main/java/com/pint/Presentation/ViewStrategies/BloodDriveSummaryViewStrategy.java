package com.pint.Presentation.ViewStrategies;

import com.pint.Data.Models.BloodDrive;
import com.pint.Presentation.ViewModels.BloodDriveSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;
import org.springframework.stereotype.Service;

/**
 * Created by Dionny on 11/26/2015.
 */
@Service
public class BloodDriveSummaryViewStrategy extends ViewStrategy<BloodDrive> {
    @Override
    protected ViewModel mapObject(BloodDrive drive) {
        return new BloodDriveSummaryViewModel(
                drive.getBloodDriveId(),
                drive.getTitle(),
                drive.getDescription(),
                drive.getStartTime(),
                drive.getEndTime(),
                drive.getAddress());
    }

    @Override
    protected ViewModel defaultMapping() {
        return new BloodDriveSummaryViewModel();
    }
}
