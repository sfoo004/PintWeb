package com.pint.BusinessLogic.Security;

import com.pint.BusinessLogic.Services.UserService;
import org.springframework.security.authentication.AuthenticationManager;

public class StatelessLoginFilterMobile extends StatelessLoginFilter {

    protected StatelessLoginFilterMobile(String urlMapping,
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
                        UserRole.DONOR
                };
    }
}
