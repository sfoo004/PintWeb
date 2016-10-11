package com.pint.Presentation.ViewStrategies;

import com.pint.Data.Models.UserNotification;
import com.pint.Presentation.ViewModels.NotificationDetailViewModel;
import com.pint.Presentation.ViewModels.NotificationSummaryViewModel;
import com.pint.Presentation.ViewModels.ViewModel;

public class NotificationDetailViewStrategy extends ViewStrategy<UserNotification> {

    @Override
    protected ViewModel mapObject(UserNotification model) {
        return new NotificationDetailViewModel(
                model.getUserNotificationId(),
                model.getHasSeen(),
                model.getNotification().getSentTime(),
                model.getNotification().getTitle(),
                model.getNotification().getShortDescription(),
                model.getNotification().getBloodDrive().getBloodDriveId(),
                model.getNotification().getBloodDrive().getTitle(),
                model.getNotification().getLongDescription());
    }

    @Override
    protected ViewModel defaultMapping() {
        return new NotificationSummaryViewModel();
    }
}
