package com.pint.BusinessLogic.Security;

import org.joda.time.DateTime;

import java.security.Timestamp;

/**
 * Created by DionnyS on 11/28/2015.
 */
public class FailureCounterHelper {
    public static int normalizeCounter(int counter, long firstFailure, long now, long interval) {
        if(firstFailure + interval < now) {
            return 0;
        }

        return counter;
    }
}
