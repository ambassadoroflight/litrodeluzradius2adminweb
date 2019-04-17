package org.unlitrodeluzcolombia.radius.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 11, 2019
 */
public class SurveyFinder extends AbstractComtorFinderFactoryI18n<Survey, Long> {

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
        headers.put("description", "Description");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Survey sponsor) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(sponsor.getId());
        row.add(sponsor.getDescription());

        return row;
    }

}
