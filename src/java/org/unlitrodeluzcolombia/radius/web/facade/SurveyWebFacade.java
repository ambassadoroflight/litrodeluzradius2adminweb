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

        QuestionWebFacade questionFacade = new QuestionWebFacade();

        for (int i = 0; i < questions.length; i++) {
            questionFacade.insert(new Question(survey.getId(), questions[i], options[i]));
        }

    }

    @Override
    public void update(Survey survey) throws BusinessLogicException {
        super.update(survey);

        String[] questions = getRequest().getParameterValues("question");
        String[] options = getRequest().getParameterValues("options");

        QuestionWebFacade questionFacade = new QuestionWebFacade();

        for (int i = 0; i < questions.length; i++) {
            questionFacade.insert(new Question(survey.getId(), questions[i], options[i]));
        }
    }
    
    

    @Override
    public Survey find(Long key) throws BusinessLogicException {
        Survey survey = super.find(key);
        if (survey == null) {
            return null;
        }
        LinkedList<Question> l;
        try {
            l = new QuestionDAOFacade().findAllByProperty("survey", survey.getId());
            survey.setQuestions(l);

        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
        return survey;
    }

}
