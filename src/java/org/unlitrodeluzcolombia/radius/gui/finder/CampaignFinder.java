package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Campaign;
import org.unlitrodeluzcolombia.radius.web.facade.CampaignWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 16, 2019
 */
public class CampaignFinder
        extends AbstractComtorFinderFactoryI18n<Campaign, Long> {

    @Override
    protected String getValueToHide(Campaign campaign) {
        return campaign.getId() + "";
    }

    @Override
    public String getValueToShow(Campaign campaign) {
        return "[" + campaign.getSponsor_name() + "] " + campaign.getDescription();
    }

    @Override
    public WebLogicFacade<Campaign, Long> getFacade() {
        return new CampaignWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("sponsor", "Patrocinador");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Campaign campaign) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(campaign.getId());
        row.add(campaign.getSponsor_name());
        row.add(campaign.getDescription());

        return row;
    }

}
