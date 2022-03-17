package Client.ServiceData;

import Client.Controller.InputController;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;
import java.util.Scanner;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -1867352852077983488L;

    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void createUser() {
        this.username = InputController.takeUsernameInput();
        this.password = InputController.takePasswordInputAndEncrypt();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
