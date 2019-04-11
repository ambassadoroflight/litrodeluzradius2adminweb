package org.unlitrodeluzcolombia.radius.ws.io;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 29, 2019
 */
public class PinSellInput implements Serializable {

    private static final long serialVersionUID = 789923350350954432L;

    private String pin;
    private long purchased_time;
    private String pin_type;
    private String seller;
    private String customer_name;
    private String customer_email;
    private long creation_date;

    public PinSellInput() {
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getPurchased_time() {
        return purchased_time;
    }

    public void setPurchased_time(long purchased_time) {
        this.purchased_time = purchased_time;
    }

    public String getPin_type() {
        return pin_type;
    }

    public void setPin_type(String pin_type) {
        this.pin_type = pin_type;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    @Override
    public String toString() {
        return "PinSellInput{"
                + "pin=" + pin
                + ", purchased_time_in_seconds=" + purchased_time
                + ", seller=" + seller
                + ", customer_name=" + customer_name
                + ", customer_email=" + customer_email
                + ", created_at=" + creation_date
                + '}';
    }

}
