package org.unlitrodeluzcolombia.radius.ws.io;

import java.io.Serializable;

/**
 *
 * @author juandiego@comtor.net
 * @since Mar 26, 2019
 */
public class PinInput implements Serializable {

    private static final long serialVersionUID = 7917862195319373882L;

    private String pin;
    private long hotspot;

    public PinInput() {
    }

    public PinInput(String pin, long hotspot) {
        this.pin = pin;
        this.hotspot = hotspot;
    }

    public long getHotspot() {
        return hotspot;
    }

    public void setHotspot(long hotspot) {
        this.hotspot = hotspot;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "HappyHourInput{" 
                + "pin=" + pin 
                + ", hotspot=" + hotspot 
                + '}';
    }

}
