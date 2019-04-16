package org.unlitrodeluzcolombia.radius.web.facade;

import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class QuestionWebFacade extends AbstractWebLogicFacade<Question, Long, QuestionDAOFacade> {

    /**
     * Elmina todas las preguntas asociadas a la encuasta
     *
     * @param surveyId
     * @return el numero de registros eliminados
     */
    public boolean deleteFromSurveyId(long surveyId) {
        getDaoFacade().deleteAllBySurveyId(surveyId);
        return true;
    }

}
