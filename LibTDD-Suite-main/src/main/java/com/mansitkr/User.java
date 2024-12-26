package com.mansitkr;

public class User {

    public static enum Role {
        LIBRARIAN,
        USER
    }
    private String userName;
    private Role role;

    public User(String userName, Role role) {
        this.userName = userName;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public Role getRole() {
        return role;
    }

    public boolean isPermittedToAddBook() {
        return role == Role.LIBRARIAN;
    }
}
