package com.pint.Data;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Security.UserHelper;
import com.pint.Data.Models.*;
import com.pint.Data.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dionny on 11/24/2015.
 */
@Service
public class DataFacade {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private BloodDriveRepository bloodDriveRepository;

    @Autowired
    private BloodDriveBaseRepository bloodDriveBaseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DonorRepository donorRepository;

    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    public User getUserById(long id) {
        return userRepository.findOne(id);
    }

    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findOne(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Hospital createHospital(String hospitalName) {
    	System.out.println("Hello");
        Hospital hospital = new Hospital(hospitalName);
        hospitalRepository.create(hospital);
        return hospital;
    }

    public void createEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Hospital getHospitalById(Long hospitalId) {
        return hospitalRepository.get(hospitalId);
    }

    public List<BloodDrive> getBloodDrivesByLocation(String city, String state) {
        return bloodDriveBaseRepository.getBloodDrivesByLocation(city, state);
    }

    public Iterable<BloodDrive> getBloodDrives() {
        return bloodDriveRepository.findAll();
    }

    public void createOrUpdateBloodDrive(BloodDrive bloodDrive) {
        bloodDriveRepository.save(bloodDrive);
    }

    public BloodDrive getBloodDriveById(long bdId) {
        return bloodDriveRepository.findOne(bdId);
    }

    public void createDonor(Donor donor) {
        donorRepository.save(donor);
    }

    public List<Employee> getHospitalEmployees(Long id) {
        return userHelper.getAllEmployees(id);
    }

    public void createNotification(Notification notification) {
        notificationRepository.create(notification);
    }

    public void createUserNotification(UserNotification userNotification) {
        notificationRepository.create(userNotification);
    }

    public List<UserNotification> getUserNotifications(User user) {
        return notificationRepository.getUserNotifications(user);
    }

    public Donor getDonorById(Long id) {
        return donorRepository.findOne(id);
    }
}