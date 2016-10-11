package com.pint.Presentation.ViewStrategies;

import com.pint.Data.Models.UserNotification;
import com.pint.Presentation.ViewModels.NotificationSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;

/**
 * Created by DionnyS on 11/28/2015.
 */
public class NotificationSummaryViewStrategy extends ViewStrategy<UserNotification> {

    @Override
    protected ViewModel mapObject(UserNotification model) {
        return new NotificationSummaryViewModel(
                model.getUserNotificationId(),
                model.getHasSeen(),
                model.getNotification().getSentTime(),
                model.getNotification().getTitle(),
                model.getNotification().getShortDescription(),
                model.getNotification().getBloodDrive().getBloodDriveId(),
                model.getNotification().getBloodDrive().getTitle());
    }

    @Override
    protected ViewModel defaultMapping() {
        return new NotificationSummaryViewModel();
    }
}

