package org.unlitrodeluzcolombia.radius.facade;

import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Question;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class QuestionDAOFacade extends ComtorDaoElementLogicFacade<Question, Long> {

    public boolean deleteAllBySurveyId(long surveyId) {
        String sql = "DELETE FROM question WHERE survey = ?";
        execute(sql, surveyId);
        return true;
    }

}
