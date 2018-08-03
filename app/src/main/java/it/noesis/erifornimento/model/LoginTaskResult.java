package it.noesis.erifornimento.model;


import java.util.List;

public class LoginTaskResult {

    private User user;
    private String error;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
