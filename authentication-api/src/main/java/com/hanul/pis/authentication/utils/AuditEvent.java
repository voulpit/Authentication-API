package com.hanul.pis.authentication.utils;

public enum AuditEvent {
    CREATE_USER(1, "Create User"),
    LOGIN(2, "Login User"),
    USER_DETAILS(3, "Get User Details"),
    UPDATE_USER(4, "Update User"),
    DELETE_USER(5, "Delete User"),
    GET_USERS(6, "Get Users List"),
    VERIFY_EMAIL(7, "Verify Email"),
    REQUEST_RESET_PASSWORD(8, "Request Password Reset"),
    RESET_PASSWORD(9, "Reset Password"),
    AUDIT_HISTORY(10, "Get Audit History"),
    GET_ADDRESS(11, "Get Address For User"),
    GET_ADDRESSES(12, "Get Addresses For User");

    private final int id;
    private final String name;

    AuditEvent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String getName(int id) {
        for (AuditEvent event : AuditEvent.class.getEnumConstants()) {
            if (event.id == id) {
                return event.getName();
            }
        }
        return null;
    }
}
