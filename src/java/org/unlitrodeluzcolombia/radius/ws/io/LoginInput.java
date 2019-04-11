package org.unlitrodeluzcolombia.radius.ws.io;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 28, 2019
 */
public class LoginInput implements Serializable {

    private static final long serialVersionUID = -1270959724900717004L;

    private String username;
    private String password;

    public LoginInput() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{"
                + "username=" + username
                + ", password=" + password
                + '}';
    }

}
