package org.unlitrodeluzcolombia.radius.ws;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 29, 2019
 */
public class LoginOutput implements Serializable {

    private static final long serialVersionUID = -523529906971332292L;

    private String message;
    private String login;
    private String name;
    private String kiosk;
    private Object response;

    public LoginOutput() {
    }

    public LoginOutput(String message, String login, String name, String kiosk, Object response) {
        this.message = message;
        this.login = login;
        this.name = name;
        this.kiosk = kiosk;
        this.response = response;
    }

    public LoginOutput(String message, Object response) {
        this(message, "", "", "", response);
    }

    public LoginOutput(String message) {
        this(message, null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKiosk() {
        return kiosk;
    }

    public void setKiosk(String kiosk) {
        this.kiosk = kiosk;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "LoginOutput{" 
                + "message=" + message 
                + ", login=" + login 
                + ", name=" + name 
                + ", kiosk=" + kiosk 
                + ", response=" + response 
                + '}';
    }

}
