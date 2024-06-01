package org.example.model;

public enum PermissionLevel {
    ADMIN,
    STANDARD;

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:
                return "Admin";
            case STANDARD:
                return "Standard";
            default:
                return super.toString();
        }
    }
}
