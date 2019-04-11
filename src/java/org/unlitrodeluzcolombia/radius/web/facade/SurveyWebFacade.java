package org.unlitrodeluzcolombia.radius.web.facade;

import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.facade.SurveyDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyWebFacade
        extends AbstractWebLogicFacade<Survey, Long, SurveyDAOFacade> {

    @Override
    public void insert(Survey survey) throws BusinessLogicException {
        super.insert(survey);

        String[] questions = getRequest().getParameterValues("question");
        String[] options = getRequest().getParameterValues("options");

        QuestionWebFacade questionFacade = new QuestionWebFacade();

        for (int i = 0; i < questions.length; i++) {
            System.out.println("question = " + questions[i]);

            System.out.println("options = " + options[i]);
            questionFacade.insert(new Question(survey.getId(), questions[i], options[i]));
        }

    }

}
