package com.irojas.oauth.oauthserver.Modals;

public class UserDTO {
    private int id;
    private String code;
    private String password;
    private String role; // Asumimos que viene como "ADMIN" o "USER"

    // Getters y setters

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}