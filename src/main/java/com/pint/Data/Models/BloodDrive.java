package com.pint.Data.Models;

import com.pint.BusinessLogic.Utilities.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.BLOODDRIVE_TABLE_NAME)
public class BloodDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long bloodDriveId;

    @NotNull
    @Size(min = 1, max = 200)
    public String title;

    @NotNull
    @Size(min = 1, max = 500)
    public String description;

    @NotNull
    @Column(name = "startTime", insertable = true)
    public Date startTime;

    @NotNull
    @Column(name = "endTime", insertable = true)
    public Date endTime;

    @NotNull
    @Size(min = 1, max = 300)
    public String address;

    @NotNull
    public int numberOfDonors = 0;

    @NotNull
    @Size(min = 1, max = 50)
    public String city;

    @NotNull
    @Size(min = 1, max = 50)
    public String state;

    @NotNull
    public int zip;

    @ManyToMany
    @JoinTable
            (
                    name = "blooddrive_employee",
                    joinColumns = {@JoinColumn(name = "bloodDriveId", referencedColumnName = "bloodDriveId")},
                    inverseJoinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")}
            )
    public Set<Employee> employees;

    @OneToOne(targetEntity = Hospital.class)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    public BloodDrive() {
        employees = new HashSet<Employee>();
    }

    public BloodDrive(long bloodDriveId, String title, String description, Date startTime, Date endTime, String address, int numberofDonors, String city, String state, int zip) {
        this.bloodDriveId = bloodDriveId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.numberOfDonors = numberofDonors;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public BloodDrive(long bloodDriveId, String title, String description, Date startTime, Date endTime, String address, int numberofDonors, String city, String state, int zip, Hospital hospitalId) {
        this.bloodDriveId = bloodDriveId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.numberOfDonors = numberofDonors;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.hospital = hospitalId;
    }

    public String getTitle() {
        return title;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    /**
     * Setter of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Setter of startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = new java.sql.Date(startTime.getTime());
    }

    /**
     * Getter of endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Setter of endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = new java.sql.Date(endTime.getTime());
    }

    public String getAddress() {
        return address;
    }

    /**
     * Setter of address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter of numberOfDonors
     */
    public int getNumberOfDonors() {
        return numberOfDonors;
    }

    /**
     * Setter of numberOfDonors
     */
    public void setNumberOfDonors(int numberOfDonors) {
        this.numberOfDonors = numberOfDonors;
    }

    /**
     * Getter of id
     */
    public long getBloodDriveId() {
        return bloodDriveId;
    }

    /**
     * Setter of id
     */
    public void setbloodDriveId(long id) {
        this.bloodDriveId = id;
    }


    public String getCity() {
        return city;
    }

    /**
     * Setter of city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter of state
     */
    public String getState() {
        return state;
    }

    /**
     * Setter of state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     */


    public int getZip() {
        return zip;
    }

    /**
     * Setter of state
     */
    public void setState(int zip) {
        this.zip = zip;
    }

    public Hospital getHospitalId() {
        return hospital;
    }

    public void setHospitalId(Hospital hospitalId) {
        this.hospital = hospitalId;
    }
}
