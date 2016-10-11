package com.pint.Data.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "user_notification")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long userNotificationId;

    @NotNull
    public boolean hasSeen;

    @OneToOne(targetEntity = Donor.class)
    @JoinColumn(name = "user_id")
    private Donor donor;

    @OneToOne(targetEntity = Notification.class)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public long getUserNotificationId() {
        return userNotificationId;
    }

    public void setUserNotificationId(long id) {
        this.userNotificationId = id;
    }

    public boolean getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public UserNotification() {
    }
}
