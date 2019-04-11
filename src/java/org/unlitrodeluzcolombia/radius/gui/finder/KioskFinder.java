package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Kiosk;
import org.unlitrodeluzcolombia.radius.web.facade.KioskWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2018
 */
public class KioskFinder extends AbstractComtorFinderFactoryI18n<Kiosk, Long> {

    @Override
    protected String getValueToHide(Kiosk kiosk) {
        return "" + kiosk.getId();
    }

    @Override
    public String getValueToShow(Kiosk kiosk) {
        return "[" + kiosk.getId() + "] " + kiosk.getName();
    }

    @Override
    protected String getTitleId(Kiosk kiosk) {
        return kiosk.getId() + "";
    }

    @Override
    public WebLogicFacade<Kiosk, Long> getFacade() {
        return new KioskWebFacade();
    }

    @Override
    protected LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("nit", "ID. Beneficiario");
        headers.put("name", "Nombre");
        headers.put("hotspot", "Hotspot");

        return headers;
    }

    @Override
    protected LinkedList<Object> getRow(Kiosk kiosk) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(kiosk.getId());
        row.add(kiosk.getNit());
        row.add(kiosk.getName());
        row.add("[" + kiosk.getId() + "] " + kiosk.getHotspot_name());

        return row;
    }

}
