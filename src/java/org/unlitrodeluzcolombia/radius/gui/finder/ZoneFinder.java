package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Zone;
import org.unlitrodeluzcolombia.radius.web.facade.ZoneWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Apr 03, 2019
 */
public class ZoneFinder extends AbstractComtorFinderFactoryI18n<Zone, Long> {

    @Override
    protected String getValueToHide(Zone zone) {
        return zone.getId() + "";
    }

    @Override
    public String getValueToShow(Zone zone) {
        return "[" + zone.getId() + "] " + zone.getName();
    }

    @Override
    public WebLogicFacade<Zone, Long> getFacade() {
        return new ZoneWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Zone hotspot) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(hotspot.getId());
        row.add(hotspot.getName());

        return row;
    }

}
