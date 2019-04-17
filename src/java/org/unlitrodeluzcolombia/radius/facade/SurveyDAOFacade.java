package org.unlitrodeluzcolombia.radius.facade;

import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Survey;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyDAOFacade extends ComtorDaoElementLogicFacade<Survey, Long> {

    public boolean haveAnswers(long surveyId) throws ComtorDaoException {
        String sql = "\n"
                + " SELECT \n"
                + "     COUNT(an.id) \n"
                + " FROM \n"
                + "     survey su \n"
                + " JOIN question qu    ON (qu.survey = su.id) \n"
                + " JOIN answer an      ON (an.question = qu.id) \n"
                + " WHERE \n"
                + "     su.id = ? \n";
        long numRows = getCountElements(sql, surveyId);
        
        return numRows > 0;
    }

}
