package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Hotspot;
import org.unlitrodeluzcolombia.radius.web.facade.HotspotWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class HotspotFinder extends AbstractComtorFinderFactoryI18n<Hotspot, Long> {

    @Override
    protected String getValueToHide(Hotspot hotspot) {
        return "" + hotspot.getId();
    }

    @Override
    public String getValueToShow(Hotspot hotspot) {
        return "[" + hotspot.getId() + "] " + hotspot.getName();
    }

    @Override
    public WebLogicFacade<Hotspot, Long> getFacade() {
        return new HotspotWebFacade();
    }

    @Override
    protected String getTitleId(Hotspot hotspot) {
        return hotspot.getId() + "";
    }

    @Override
    protected LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");
        headers.put("ip_address", "Dirección IP");

        return headers;
    }

    @Override
    protected LinkedList<Object> getRow(Hotspot hotspot) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(hotspot.getId());
        row.add(hotspot.getName());
        row.add(hotspot.getIp_address());

        return row;
    }

}
