package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.SurveyDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyWebFacade extends AbstractWebLogicFacade<Survey, Long, SurveyDAOFacade> {

    @Override
    public void insert(Survey survey) throws BusinessLogicException {
        super.insert(survey);

        String[] questions = getRequest().getParameterValues("question");
        String[] options = getRequest().getParameterValues("options");
        String[] types = getRequest().getParameterValues("type");

        QuestionDAOFacade questionDAOFacade = new QuestionDAOFacade();;

        for (int i = 0; i < questions.length; i++) {
            try {
                questionDAOFacade.create(new Question(survey.getId(), questions[i], options[i], types[i]));
            } catch (ComtorDaoException ex) {
                Logger.getLogger(SurveyWebFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void update(Survey survey) throws BusinessLogicException {
        super.update(survey);
        QuestionDAOFacade questionDAOFacade = new QuestionDAOFacade();

        try {

            String[] questions = getRequest().getParameterValues("question");
            String[] options = getRequest().getParameterValues("options");
            String[] types = getRequest().getParameterValues("type");

            if (questions == null) {
                return;
            }

            if (survey == null) {
                System.out.println("ES IGUAL A NULL");
            } else {
                System.out.println("NO ES IGUAL A NULL " + survey.toString());
            }
            questionDAOFacade.deleteAllBySurveyId(survey.getId());

            for (int i = 0; i < questions.length; i++) {
                questionDAOFacade.create(new Question(survey.getId(), questions[i], options[i], types[i]));
            }
        } catch (ComtorDaoException ex) {
            Logger.getLogger(SurveyWebFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Survey find(Long key) throws BusinessLogicException {
        Survey survey = super.find(key);
        if (survey == null) {
            return null;
        }
        return survey;
    }

    public boolean isEditable(Survey survery) {
        try {
            return getDaoFacade().haveAnswers(survery.getId());
        } catch (ComtorDaoException ex) {
            Logger.getLogger(SurveyWebFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
