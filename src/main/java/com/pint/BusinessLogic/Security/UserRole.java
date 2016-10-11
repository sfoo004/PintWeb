package com.pint.BusinessLogic.Security;

public enum UserRole {
    MANAGER, COORDINATOR, NURSE, DONOR;

    public UserAuthority asAuthorityFor(final User user) {
        final UserAuthority authority = new UserAuthority();
        authority.setAuthority("ROLE_" + toString());
        authority.setUser(user);
        return authority;
    }

    public static UserRole valueOf(final UserAuthority authority) {
        switch (authority.getAuthority()) {
            case "ROLE_MANAGER":
                return MANAGER;
            case "ROLE_COORDINATOR":
                return COORDINATOR;
            case "ROLE_NURSE":
                return NURSE;
            case "ROLE_DONOR":
                return DONOR;
        }
        throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
    }
}
