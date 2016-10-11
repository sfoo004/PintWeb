package com.pint.Presentation.ViewModels;

import com.pint.BusinessLogic.Services.EmployeeService;

/**
 * Created by Dionny on 11/27/2015.
 */
public class EmployeeSummaryViewModel extends ViewModel {
    public Long userId;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String role;

    public EmployeeSummaryViewModel(Long userId, String firstName, String lastName, String phoneNumber, String email, String role){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    public EmployeeSummaryViewModel() {
    }
}
