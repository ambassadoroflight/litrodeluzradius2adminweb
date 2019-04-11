package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Sponsor;
import org.unlitrodeluzcolombia.radius.web.facade.SponsorWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 04, 2019
 */
public class SponsorFinder
        extends AbstractComtorFinderFactoryI18n<Sponsor, Long> {

    @Override
    protected String getValueToHide(Sponsor sponsor) {
        return sponsor.getId() + "";
    }

    @Override
    public String getValueToShow(Sponsor sponsor) {
        return "[" + sponsor.getId() + "] " + sponsor.getName();
    }

    @Override
    public WebLogicFacade<Sponsor, Long> getFacade() {
        return new SponsorWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Sponsor sponsor) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(sponsor.getId());
        row.add(sponsor.getName());

        return row;
    }

}
