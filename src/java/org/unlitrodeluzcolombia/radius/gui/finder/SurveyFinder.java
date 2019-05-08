package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.radius.element.Campaign;
import net.comtor.radius.facade.CampaignDAOFacade;
import net.comtor.radius.facade.ZoneDAOFacade;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 11, 2019
 */
public class SurveyFinder extends AbstractComtorFinderFactoryI18n<Survey, Long> {

    private static final Logger LOG = Logger.getLogger(SurveyFinder.class.getName());

    private CampaignDAOFacade campaignFacade = new CampaignDAOFacade();

    @Override
    protected String getValueToHide(Survey survey) {
        return survey.getId() + "";
    }

    @Override
    public String getValueToShow(Survey survey) {
        return survey.getDescription();
    }

    @Override
    public WebLogicFacade<Survey, Long> getFacade() {
        return new SurveyWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("sponsor", "Patrocinador");
        headers.put("campaign", "Campaña");
        headers.put("description", "Description");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Survey survey) {
        Campaign campaign = null;

        try {
            campaign = campaignFacade.find(survey.getCampaign());
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        LinkedList<Object> row = new LinkedList<>();
        row.add(survey.getId());
        row.add(campaign.getSponsor_name());
        row.add(survey.getCampaign_description());
        row.add(survey.getDescription());

        return row;
    }

}
