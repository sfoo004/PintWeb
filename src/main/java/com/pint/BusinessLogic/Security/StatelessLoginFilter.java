package com.pint.BusinessLogic.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pint.BusinessLogic.Services.UserService;
import org.joda.time.DateTime;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

public abstract class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private UserService userService;
    private final UserDetailsService userDetailsService;

    protected StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
                                   UserDetailsService userDetailsService, UserService userService, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping));
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userService = userService;
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

        User realUser = userService.getUserByEmail(user.getUsername());

        if (realUser != null) {
            boolean acceptedRole = false;
            UserRole[] roles = acceptedRoles();
            for (UserRole role :
                    roles) {
                if (realUser.hasRole(role)) {
                    acceptedRole = true;
                    break;
                }
            }

            if (!acceptedRole) {
                throw new BadCredentialsException("Incorrect credentials.");
            }

            if (!realUser.isAccountNonLocked()) {
                // Has it been 24 hours since the first failure?
                if (realUser.getFirstFailTime().getTime() + 1440000 < DateTime.now().toDate().getTime()) {
                    // Unlock the account.
                    realUser.unlockAccount();
                    realUser.setFailCounter(0);
                    userService.updateUser(realUser);
                } else {
                    throw new BadCredentialsException("Incorrect credentials.");
                }
            }
        }

        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());
        try {
            return getAuthenticationManager().authenticate(loginToken);
        } catch (AuthenticationException e) {

            if (realUser != null) {
                int currentFailures = realUser.getFailCounter();
                if (currentFailures == 0) {
                    realUser.setFirstFailTime(new Timestamp(DateTime.now().toDate().getTime()));
                } else {
                    // First, normalize the failures. It's possible that more than 24 hours has elapsed.
                    currentFailures = FailureCounterHelper.normalizeCounter(currentFailures,
                            realUser.getFirstFailTime().getTime(),
                            DateTime.now().toDate().getTime(),
                            DateTime.now().minusHours(24).toDate().getTime());

                    if (currentFailures >= 3) {
                        realUser.lockAccount();
                    }
                }

                realUser.setFailCounter(++currentFailures);
                userService.updateUser(realUser);
            }

            throw new BadCredentialsException("Incorrect credentials.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // Lookup the complete User object from the database and create an Authentication for it
        final User authenticatedUser = userDetailsService.loadUserByUsername(authentication.getName());

        authenticatedUser.setFailCounter(0);
        userService.updateUser(authenticatedUser);

        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        // Add the custom token as HTTP header to the response
        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        // Add the authentication to the Security context
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }

    protected abstract UserRole[] acceptedRoles();
}