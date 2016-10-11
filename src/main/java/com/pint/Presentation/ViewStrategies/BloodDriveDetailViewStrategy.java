package com.pint.Presentation.ViewStrategies;

import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Employee;
import com.pint.Presentation.ViewModels.BloodDriveDetailCoordinatorViewModel;
import com.pint.Presentation.ViewModels.BloodDriveDetailNurseViewModel;
import com.pint.Presentation.ViewModels.ViewModel;

import java.util.List;

/**
 * Created by Dionny on 11/26/2015.
 */
public class BloodDriveDetailViewStrategy extends ViewStrategy<BloodDrive> {

    private List<Employee> assignedNurses = null;
    private List<Employee> unassignedNurses = null;
    private ViewModel viewModel;

    public BloodDriveDetailViewStrategy(List<Employee> assignedNurses, List<Employee> unassignedNurses){
        this.assignedNurses = assignedNurses;
        this.unassignedNurses = unassignedNurses;
    }

    public BloodDriveDetailViewStrategy() {
    }

    public BloodDriveDetailViewStrategy(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected ViewModel mapObject(BloodDrive drive) {
        if(assignedNurses == null && unassignedNurses == null) {
            return new BloodDriveDetailNurseViewModel(
                    drive.getBloodDriveId(),
                    drive.getTitle(),
                    drive.getDescription(),
                    drive.getStartTime(),
                    drive.getEndTime(),
                    drive.getHospitalId().getName(),
                    drive.getAddress(),
                    drive.getNumberOfDonors(),
                    viewModel);
        }

        return new BloodDriveDetailCoordinatorViewModel(
                drive.getBloodDriveId(),
                drive.getTitle(),
                drive.getDescription(),
                drive.getStartTime(),
                drive.getEndTime(),
                drive.getAddress(),
                drive.getNumberOfDonors(),
                assignedNurses,
                unassignedNurses);
    }

    @Override
    protected ViewModel defaultMapping() {
        return new BloodDriveDetailCoordinatorViewModel();
    }
}
