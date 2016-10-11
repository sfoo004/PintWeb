package com.pint.Presentation.ViewStrategies;

import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.Data.Models.Employee;
import com.pint.Presentation.ViewModels.EmployeeSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;
import org.springframework.stereotype.Service;

/**
 * Created by Dionny on 11/27/2015.
 */
@Service
public class EmployeeSummaryViewStrategy extends ViewStrategy<Employee> {
    @Override
    protected ViewModel mapObject(Employee model) {
        return new EmployeeSummaryViewModel(model.getUserId(),
                model.getFirstName(),
                model.getLastName(),
                model.getPhoneNumber(),
                model.getEmail(),
                Utils.toTitleCase(model.getRole()));
    }

    @Override
    protected ViewModel defaultMapping() {
        return new EmployeeSummaryViewModel();
    }
}
