package com.capstone.pod.constant.role;

public final class RolePreAuthorize {

    public static final String ROLE_USER="hasRole('USER')";
    public static final String ROLE_ADMIN="hasRole('ADMIN')";
    public static final String ROLE_ADMIN_AND_FACTORY="hasAnyRole('ADMIN','FACTORY')";
    public static final String ROLE_USER_AND_FACTORY="hasAnyRole('USER','FACTORY')";
    public static final String ROLE_ADMIN_AND_USER="hasAnyRole('USER','ADMIN')";
    public static final String ROLE_ADMIN_AND_USER_AND_FACTORY="hasAnyRole('USER','ADMIN','FACTORY')";

    public static final String ROLE_FACTORY="hasRole('FACTORY')";

}
