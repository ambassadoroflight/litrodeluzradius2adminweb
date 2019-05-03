package org.unlitrodeluzcolombia.radius.gui.hotspot.microservices;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.radius.element.Country;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.element.Zone;
import net.comtor.radius.facade.CountryDAOFacade;
import net.comtor.radius.facade.ZoneDAOFacade;
import net.comtor.util.StringUtil;
import org.unlitrodeluzcolombia.radius.gui.hotspot.commons.MapMarker;
import org.unlitrodeluzcolombia.radius.gui.hotspot.commons.MapMarkerInfoWindow;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 26, 2019
 */
@Path("hotspot_services")
public class HotspotServices {

    private static final Logger LOG = Logger.getLogger(HotspotServices.class.getName());

    /**
     * WS que regresa lista con marcadores de hotspot para el mapa.
     *
     * @author juandiego@comtor.net
     * @since 1.8
     * @version Abr 25, 2019
     * @return
     */
    @GET
    @Path("/map/get_hotspots")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapMarker> getHotspotsMapMarkers(
            @QueryParam("sponsor") long sponsor,
            @QueryParam("zone") long zone,
            @QueryParam("country") String country,
            @QueryParam("start_date") String start_date,
            @QueryParam("end_date") String end_date) {
        java.sql.Date start = RequestBasicValidator.getDateFromValue(start_date);
        System.out.println("start = " + start);
        java.sql.Date end = RequestBasicValidator.getDateFromValue(end_date);
        System.out.println("end = " + end);

        List<MapMarker> markers = new LinkedList<>();
        List<Hotspot> hotspots = new LinkedList<>();

        try {
            hotspots = new HotspotWebFacade().find(sponsor, zone, country,
                    start, end);
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (!hotspots.isEmpty()) {
            MapMarker marker;

            for (Hotspot hotspot : hotspots) {
                double lat = hotspot.getLatitude();
                double lon = hotspot.getLongitude();
                String w3w = hotspot.getWhat3words();
                String calledStationID = hotspot.getCalled_station_id();

                marker = new MapMarker.Builder()
                        .name(hotspot.getCalled_station_id())
                        .key(hotspot.getId() + "")
                        .latitude(lat)
                        .longitude(lon)
                        .info(new MapMarkerInfoWindow(calledStationID, w3w, (lat
                                        + ", " + lon)).getHtml())
                        .build();

                markers.add(marker);
            }

        }

        return markers;
    }

    /**
     * WS que regresa el nombre del pais según su código ISO
     *
     * @author juandiego@comtor.net
     * @since 1.8
     * @version May 3, 2019
     * @param iso
     * @return
     */
    @GET
    @Path("/map/get_country")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCountry(@QueryParam("iso") String iso) {
        if (!StringUtil.isValid(iso)) {
            return "";
        }

        try {
            Country country = new CountryDAOFacade().find(iso);

            return country.getName();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return "";
        }
    }

    /**
     * WS que regresa un listao de zonas correspondientes a un pais
     *
     * @author juandiego@comtor.net
     * @since 1.8
     * @version May 3, 2019
     * @param country
     * @return
     */
    @GET
    @Path("/map/get_zones")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Zone> getZones(@QueryParam("country") String country) {
        try {
            if (StringUtil.isValid(country)) {
                return new ZoneDAOFacade().findAllByProperty("country", country);
            }

            return new ZoneDAOFacade().findAll();
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return null;
    }
}
