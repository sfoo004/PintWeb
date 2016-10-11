package com.pint.Data.Models;

import com.pint.BusinessLogic.Utilities.Constants;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = Constants.DONOR_TABLE_NAME)
public class Donor {

    @NotNull
    @Id
    private Long userId;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String city;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String state;

    @NotNull
    private int zip;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String country;

    public Donor() {
    }

    public Donor(String country, String city, String state, int zip) {
        this.country = country;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getZip() {
        return zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
