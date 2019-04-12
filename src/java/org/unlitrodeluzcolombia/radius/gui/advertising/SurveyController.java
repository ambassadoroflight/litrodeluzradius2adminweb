package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.form.HtmlRadioGroup;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlContainer;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlHr;
import net.comtor.html.HtmlSpan;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlRadio;
import net.comtor.html.form.HtmlTextArea;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.enums.QuestionType;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyController
        extends AbstractComtorFacadeAdministratorControllerI18n<Survey, Long> {

    private static final Logger LOG = Logger.getLogger(SurveyController.class.getName());

    @Override
    public String getEntityName() {
        return "Encuestas";
    }

    @Override
    public WebLogicFacade<Survey, Long> getLogicFacade() {
        return new SurveyWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, Survey survey) throws BusinessLogicException {

        if (survey != null) {
            long surveyId = survey.getId();
            form.addInputHidden("id", surveyId);
        }

        HtmlTextArea description = new HtmlTextArea("description", 40, 5, 128);
        form.addField("Descripción", description, null);
        form.addSubTitle("Preguntas");

        HtmlDiv questions_area = new HtmlDiv("questions_area");
        if (survey != null) {
            survey.getQuestions().forEach((question) -> {
                questions_area.add(getQuestionTag(question.getQuestion(), question.getOptions()));
            });
        }
        form.addRowInOneCell(questions_area);

        HtmlContainer container = new HtmlContainer();
        container.add(getAddQuestionButton("Agregar pregunta abierta", QuestionType.OPEN_QUESTION.toString(), "#D0F5A9"))
                .add(getAddQuestionButton("Agregar de selección simple", QuestionType.SINGLE.toString(), "#A9E2F3"))
                .add(getAddQuestionButton("Agregar de selección múltiple", QuestionType.MULTIPLE.toString(), "#D0A9F5"))
                .add(new HtmlHr());
        form.addRowInOneCell(container);

    }

    private HtmlButton getAddQuestionButton(String title, String keyword, String color) {
        HtmlButton addQuestionButton = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "add_question", title);
        addQuestionButton.addAttribute("class", "ajaxGet");
        addQuestionButton.addAttribute("title", title);
        addQuestionButton.addAttribute("style", "color:" + color);
        addQuestionButton.addAttribute("endpoint", "webservices/survey_service/question_tag/" + keyword);
        addQuestionButton.addAttribute("action_type", "append");
        return addQuestionButton;
    }

    @Override
    public void initFormView(AdministrableForm form, Survey survey) {
        try {
            HtmlText field = new HtmlText(survey.getId());
            form.addField("ID", field, null);

            field = new HtmlText(survey.getDescription());
            form.addField("Descripción", field, null);

            form.addSubTitle("Preguntas");

            LinkedList<Question> questions = new QuestionDAOFacade()
                    .findAllByProperty("survey", survey.getId());

            int i = 1;

            for (Question question : questions) {
                field = new HtmlText(question.getQuestion());
                form.addField("Pregunta #" + i, field, null);

                HtmlRadioGroup options = new HtmlRadioGroup();

                for (String option : question.getOptionsArray()) {
                    options.addElement(new HtmlRadio("options", option, option));
                    options.addElement(new HtmlBr());
                }

                form.addField("", options, null);

                i++;
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

//    @Override
//    public Survey getObjectFromRequest(HttpServletMixedRequest request)
//            throws ComtorAdministrableHtmlException {
//        Survey survey = super.getObjectFromRequest(request);
//        long id = survey.getId();
//
//        if (id <= 0) {
//            return survey;
//        }
//
//        try {
//            LinkedList<Question> questions = new QuestionDAOFacade().findAllByProperty("survey", id);
//            survey.setQuestions(questions);
//
//            return survey;
//        } catch (ComtorDaoException ex) {
//            throw new ComtorAdministrableHtmlException(ex);
//        }
//    }
    @Override
    public String getAddPrivilege() {
        return "ADD_SPONSOR";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_SPONSOR";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_SPONSOR";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_SPONSOR";
    }

    @Override
    public String getFormName() {
        return "survey_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("description", "Descripción");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Survey survey) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(survey.getId());
        row.add(survey.getDescription());

        return row;
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    public HtmlElement getQuestionTag(String question, String options) {

        HtmlInputText inputText = new HtmlInputText("question", 32, 512);
        inputText.addAttribute("required", "required");
        inputText.setValue(question);

        HtmlDiv divForm = new HtmlDiv();
        divForm.setClass("DivFormField");
        divForm.add(new HtmlSpan("question_error"))
                .add(new HtmlSpan("sp1", "Preguntas"))
                .add(new HtmlSpan().add(inputText))
                .add(new HtmlSpan());

        HtmlTextArea textArea = new HtmlTextArea("options", "", 40, 5, 128);
        textArea.addAttribute("onkeyup", "return ismaxlength(this)");
        textArea.addAttribute("required", "required");
        textArea.setValue(options);

        HtmlDiv divTxA = new HtmlDiv();
        divTxA.setClass("DivFormField")
                .add(new HtmlSpan("option_error"))
                .add(new HtmlSpan("", "Opciones (separadas por barra vertical | )"))
                .add(textArea);

        HtmlButton button = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "delete_question", "Eliminar");
        button.addAttribute("class", "deleteSection");

        return new HtmlDiv().addAttribute("class", "div-answer-content")
                .add(new HtmlBr())
                .add(divForm)
                .add(divTxA)
                .add(button)
                .add(new HtmlHr());
    }

}
