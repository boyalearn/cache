package com.test.jackson;

public class Auth {

    public Auth() {
    }

    private String role;

    private String menu;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Auth(String role, String menu) {
        this.role = role;
        this.menu = menu;
    }

}
