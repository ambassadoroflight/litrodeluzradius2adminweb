package org.unlitrodeluzcolombia.radius.web.facade;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
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

    private static final Logger LOG = Logger.getLogger(SurveyWebFacade.class.getName());

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Survey survey) {
        return validate(survey);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Survey survey) {
        if (!isEditable(survey)) {
            LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();
            exceptions.add(new ObjectValidatorException("description", "Esta encuesta "
                    + "no es editable porque ya ha sido respondida."));
            return exceptions;
        }

        return validate(survey);
    }

    @Override
    public void insert(Survey survey) throws BusinessLogicException {
        super.insert(survey);

        String[] questions = getRequest().getParameterValues("question");
        String[] options = getRequest().getParameterValues("options");
        String[] types = getRequest().getParameterValues("type");

        QuestionDAOFacade questionDAOFacade = new QuestionDAOFacade();;

        for (int i = 0; i < questions.length; i++) {
            try {
                questionDAOFacade.create(new Question(survey.getId(), questions[i],
                        options[i], types[i]));
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
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
                questionDAOFacade.create(new Question(survey.getId(), questions[i],
                        options[i], types[i]));
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
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
            return getDaoFacade().hasAnswers(survery.getId());
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return false;
    }

    private LinkedList<ObjectValidatorException> validate(Survey survey) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (survey.getCampaign() <= 0) {
            exceptions.add(new ObjectValidatorException("campaign", "Debe indicar "
                    + "a qué campaña pertenece esta encuesta."));
        }

        String[] questions = getRequest().getParameterValues("question");
        String[] options = getRequest().getParameterValues("options");

        if (((questions == null) || (questions.length == 0))
                || ((options == null) || (options.length == 0))) {
            String message = "Debe agregar preguntas y opciones a la encuesta";

            exceptions.add(new ObjectValidatorException("description", message));
            exceptions.add(new ObjectValidatorException("question", message));
            exceptions.add(new ObjectValidatorException("options", message));
        }

        return exceptions;
    }

}
