package com.pint.Data.Models;

import com.pint.BusinessLogic.Utilities.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = Constants.HOSPITAL_TABLE_NAME)
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long hospitalId;

    @NotNull
    @Size(min = 3, max = 200)
    private String hospitalName;

    public Hospital() {

    }

    public Hospital(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Hospital(long hospitalId, String hospitalName) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
    }

    public long getId() {
        return hospitalId;
    }

    public void setId(long id) {
        this.hospitalId = id;
    }

    public String getName() {
        return hospitalName;
    }

    public void setName(String name) {
        this.hospitalName = name;
    }
}
