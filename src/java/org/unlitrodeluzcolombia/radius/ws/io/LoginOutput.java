package org.unlitrodeluzcolombia.radius.ws.io;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 29, 2019
 */
public class LoginOutput implements Serializable {

    private static final long serialVersionUID = -523529906971332292L;

    private String message;

    public LoginOutput() {
    }

    public LoginOutput(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginOutput{"
                + "message=" + message
                + '}';
    }

}
