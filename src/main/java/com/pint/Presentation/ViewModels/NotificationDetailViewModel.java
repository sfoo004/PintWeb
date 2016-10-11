package com.pint.Presentation.ViewModels;

import java.sql.Timestamp;

/**
 * Created by DionnyS on 11/28/2015.
 */
public class NotificationDetailViewModel extends NotificationSummaryViewModel {

    public String longDescription;

    public NotificationDetailViewModel() {
    }

    public NotificationDetailViewModel(Long notificationId,
                                       boolean hasSeen,
                                       Timestamp sentTime,
                                       String title,
                                       String shortDescription,
                                       Long bloodDriveId,
                                       String bloodDriveTitle,
                                       String longDescription) {
        super(notificationId, hasSeen, sentTime, title, shortDescription, bloodDriveId, bloodDriveTitle);
        this.longDescription = longDescription;
    }
}
