package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.AdvertisingCampaign;
import org.unlitrodeluzcolombia.radius.web.facade.AdvertisingCampaignWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 04, 2019
 */
public class AdvertisingCampaignFinder
        extends AbstractComtorFinderFactoryI18n<AdvertisingCampaign, Long> {

    @Override
    protected String getValueToHide(AdvertisingCampaign campaign) {
        return campaign.getId() + "";
    }

    @Override
    public String getValueToShow(AdvertisingCampaign campaign) {
        return "[" + campaign.getId() + "] " + campaign.getSponsor_name();
    }

    @Override
    public WebLogicFacade<AdvertisingCampaign, Long> getFacade() {
        return new AdvertisingCampaignWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("sponsor", "Patrocinador");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(AdvertisingCampaign campaign) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(campaign.getId());
        row.add(campaign.getSponsor_name());

        return row;
    }

}
