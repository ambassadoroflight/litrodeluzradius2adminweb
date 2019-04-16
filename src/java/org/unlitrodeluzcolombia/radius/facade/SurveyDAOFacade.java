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
        String sql = "select count(an.id) from survey su join question qu on (qu.survey = su.id) join answer an on (an.question = qu.id) where su.id = ?";
        long numRows = getCountElements(sql, surveyId);
        return numRows > 0;
    }

}
