package org.unlitrodeluzcolombia.radius.gui.hotspot.microservices;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import net.comtor.exception.BusinessLogicException;
import net.comtor.radius.element.Hotspot;
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
     * @return
     */
    @GET
    @Path("/map/get_hotspots")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapMarker> getHotspotsMapMarkers(@QueryParam("sponsor") long sponsor) {
        List<MapMarker> markers = new LinkedList<>();
        List<Hotspot> hotspots = new LinkedList<>();

        try {
            hotspots = new HotspotWebFacade().findBySponsor(sponsor);
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

}
