package org.unlitrodeluzcolombia.radius.gui.hotspot;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.framework.html.administrable.ComtorMessageHelperI18n;
import net.comtor.framework.jsptag.HtmlGuiInterface;
import net.comtor.framework.maps.HtmlGoogleMap;
import net.comtor.html.HtmlContainer;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlImg;
import net.comtor.radius.element.Hotspot;
import net.comtor.radius.facade.HotspotDAOFacade;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 26, 2019
 */
public class HotspotsMapPage extends HtmlGuiInterface {

    private static final Logger LOG = Logger.getLogger(HotspotsMapPage.class.getName());

    private static final String TITLE = "Mapa Hotspots";
    private LinkedList<Hotspot> hotspots;

    @Override
    public String getHtml() {
        HtmlContainer mainContainer = new HtmlContainer();

        HtmlDiv controller_title = new HtmlDiv(null, "controller_title", new HtmlImg(""));
        controller_title.addString("Hospots");

        mainContainer.addElement(controller_title);

        HtmlDiv map_container = new HtmlDiv("map_container");
        mainContainer.addElement(map_container);

        try {
            double centerX = 0;
            double centerY = 0;

            hotspots = new HotspotDAOFacade().findAll();

            if ((hotspots == null) || hotspots.isEmpty()) {
                return ComtorMessageHelperI18n.getErrorForm(TITLE, "index.jsp",
                        "No se encontraron ubicaciones para mostrar en el mapa.", request).getHtml();
            }

            for (Hotspot hotspot : hotspots) {
                centerX += hotspot.getLatitude();
                centerY += hotspot.getLongitude();
            }

            centerX /= hotspots.size();
            centerY /= hotspots.size();

            HtmlGoogleMap map = new HtmlGoogleMap(GlobalWeb.GOOGLE_MAPS_KEY, centerX, centerY);
            map.addAjaxMarkerList("/litrodeluz/radius/webservices/hotspot_services/map/get_hotspots", false);

            map_container.addElement(map);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return ComtorMessageHelperI18n.getErrorForm(TITLE, "index.jsp",
                    "<b>Se ha generado un error en la generación de esta función. Por favor, "
                    + "reporte al administrador del sistema:</b><br><p>" + ex
                    + "</p>", request).getHtml();
        }

        return mainContainer.getHtml();

    }

    @Override
    public boolean requireComtorSession() {
        return true;
    }

}
