package com.pint.Data.Models;

import com.pint.BusinessLogic.Utilities.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = Constants.NOTIFICATION_TABLE_NAME)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long notificationId;

    @NotNull
    @Size(min = 1, max = 200)
    public String title;

    @NotNull
    @Size(min = 1, max = 200)
    public String shortDescription;

    @NotNull
    @Size(min = 1, max = 500)
    public String longDescription;

    @OneToOne(targetEntity = BloodDrive.class)
    @JoinColumn(name = "blood_drive_id")
    private BloodDrive bloodDrive;

    @NotNull
    @Column(name = "sentTime")
    private Timestamp sentTime;

    public long getId() {
        return notificationId;
    }

    public void setId(long id) {
        this.notificationId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public BloodDrive getBloodDrive() {
        return bloodDrive;
    }

    public void setBloodDrive(BloodDrive bloodDrive) {
        this.bloodDrive = bloodDrive;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public Notification() {
    }

    public Notification(long id, String title, String shortDescription, String longDescription) {
        this.notificationId = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }
}
