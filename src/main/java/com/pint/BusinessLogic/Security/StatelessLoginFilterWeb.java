package com.pint.BusinessLogic.Security;

import com.pint.BusinessLogic.Services.UserService;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Created by DionnyS on 11/28/2015.
 */
public class StatelessLoginFilterWeb extends StatelessLoginFilter {

    protected StatelessLoginFilterWeb(String urlMapping,
                                      TokenAuthenticationService tokenAuthenticationService,
                                      UserDetailsService userDetailsService,
                                      UserService userService,
                                      AuthenticationManager authManager) {
        super(urlMapping, tokenAuthenticationService, userDetailsService, userService, authManager);
    }

    @Override
    protected UserRole[] acceptedRoles() {
        return new UserRole[]
                {
                        UserRole.COORDINATOR,
                        UserRole.NURSE,
                        UserRole.MANAGER
                };
    }
}

