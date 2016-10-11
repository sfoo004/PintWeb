package com.pint.BusinessLogic.Services;

import com.pint.BusinessLogic.Security.User;
import com.pint.Data.DataFacade;
import com.pint.Data.Models.BloodDrive;
import com.pint.Data.Models.Donor;
import com.pint.Data.Models.Notification;
import com.pint.Data.Models.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DionnyS on 11/28/2015.
 */
@Service
public class NotificationService {
    @Autowired
    private DataFacade dataFacade;

    public NotificationService() {
    }

    public NotificationService(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public Notification createNotification(BloodDrive drive,
                                           Date sentTime,
                                           String title,
                                           String shortDescription,
                                           String longDescription) {
        Notification notification = new Notification();
        notification.setBloodDrive(drive);
        notification.setTitle(title);
        notification.setShortDescription(shortDescription);
        notification.setSentTime(new Timestamp(sentTime.getTime()));
        notification.setLongDescription(longDescription);

        dataFacade.createNotification(notification);

        return notification;
    }

    public UserNotification createUserNotification(Notification notification, Donor user) {
        UserNotification userNotification = new UserNotification();
        userNotification.setNotification(notification);
        userNotification.setDonor(user);

        dataFacade.createUserNotification(userNotification);

        return userNotification;
    }

    public List<UserNotification> getUserNotifications(User user) {
        return dataFacade.getUserNotifications(user);
    }

    public List<UserNotification> getUserNotifications(User user, Long bloodDriveId) {
        List<UserNotification> output = new ArrayList<>();
        List<UserNotification> userNotifications = dataFacade.getUserNotifications(user);
        for (UserNotification userNotification :
                userNotifications) {
            if (userNotification.getNotification().getBloodDrive().getBloodDriveId() == bloodDriveId) {
                output.add(userNotification);
            }
        }

        return output;
    }
}
