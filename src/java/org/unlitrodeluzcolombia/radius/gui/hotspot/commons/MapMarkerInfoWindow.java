package org.unlitrodeluzcolombia.radius.gui.hotspot.commons;

import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlH3;
import net.comtor.html.HtmlSpan;

/**
 *
 * @author juandiego@comtor.net
 * @since
 * @version Apr 26, 2019
 */
public class MapMarkerInfoWindow extends HtmlDiv {

    private final HtmlDiv content = new HtmlDiv();

    public MapMarkerInfoWindow(String calledStationId, String w3w, String coordenates) {
        addAttribute("class", "gps_marker_info_window");

        addElement(new HtmlH3("Hotspot: " + calledStationId));
        addElement(new HtmlSpan("", "<b>" + w3w + "</b>"));
        addElement(new HtmlSpan("", coordenates));

    }

    public void addContent(HtmlElement item) {
        content.addElement(new HtmlSpan("", item));
    }

}
