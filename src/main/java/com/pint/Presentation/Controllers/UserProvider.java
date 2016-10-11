package com.pint.Presentation.Controllers;

import com.pint.BusinessLogic.Security.User;

/**
 * Created by DionnyS on 11/29/2015.
 */
public interface UserProvider {
    User getUser() throws Exception;
}
