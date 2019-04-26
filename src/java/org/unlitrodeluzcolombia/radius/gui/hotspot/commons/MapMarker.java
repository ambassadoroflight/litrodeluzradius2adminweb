package org.unlitrodeluzcolombia.radius.gui.hotspot.commons;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 26, 2019
 */
@XmlRootElement
public class MapMarker implements Serializable {

    private static final long serialVersionUID = 1280488826002316521L;

    private String name;
    private String key;
    private String event;
    private double latitude;
    private double longitude;
    private String iconURL;
    private String info;

    public MapMarker() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    @Override
    public String toString() {
        return "MapMarker{"
                + "name=" + name
                + ", key=" + key
                + ", event=" + event
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", iconURL=" + iconURL
                + ", info=" + info
                + '}';
    }

    public static class Builder {

        private String name;
        private String key;
        private String event;
        private double latitude;
        private double longitude;
        private String iconURL;
        private String info;

        public Builder() {

        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        public Builder key(String key) {
            this.key = key;

            return this;
        }

        public Builder event(String event) {
            this.event = event;

            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;

            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;

            return this;
        }

        public Builder iconURL(String iconURL) {
            this.iconURL = iconURL;

            return this;
        }

        public Builder info(String info) {
            this.info = info;

            return this;
        }

        public MapMarker build() {
            MapMarker marker = new MapMarker();
            marker.setName(name);
            marker.setKey(key);
            marker.setEvent(event);
            marker.setLatitude(latitude);
            marker.setLongitude(longitude);
            marker.setIconURL(iconURL);
            marker.setInfo(info);

            return marker;

        }
    }
}
