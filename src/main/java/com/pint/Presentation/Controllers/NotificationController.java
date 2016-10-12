package com.pint.Presentation.Controllers;

import com.pint.BusinessLogic.Security.User;
import com.pint.BusinessLogic.Services.NotificationService;
import com.pint.Data.Models.UserNotification;
import com.pint.Presentation.ViewStrategies.NotificationDetailViewStrategy;
import com.pint.Presentation.ViewStrategies.NotificationSummaryViewStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    private Session session;

    public NotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public NotificationController() {
        this.session = new Session();
    }

    /**
     * MOCK notificationService
     * MOCK NotificationDetailViewStrategy
     * @return
     */
    @RequestMapping("/api/donor/getUserNotifications")
    @ResponseBody
    public Object getUserNotification() {
        List<UserNotification> userNotifications;

        try {
            User user = session.getUser();
            if (user.isDonor()) {
                userNotifications = notificationService.getUserNotifications(user);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new NotificationSummaryViewStrategy().CreateViewModel(userNotifications);
    }

    /**
     *  MOCK notificationService
     *  MOCK NotificationDetailViewStrategy
     * @param id
     * @return
     */
    @RequestMapping("/api/donor/getBloodDriveUserNotifications/{id}")
    @ResponseBody
    public Object getUserNotifications(@PathVariable("id") Long id) {
        List<UserNotification> userNotifications;

        try {
            User user = session.getUser();
            if (user.isDonor()) {
                userNotifications = notificationService.getUserNotifications(user, id);
            } else {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new NotificationDetailViewStrategy().CreateViewModel(userNotifications);
    }
}
