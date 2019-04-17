package org.unlitrodeluzcolombia.radius.gui.advertising;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.advanced.html.form.HtmlRadioGroup;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlContainer;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlHr;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlInput;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlRadio;
import net.comtor.html.form.HtmlTextArea;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.radius.element.Campaign;
import org.unlitrodeluzcolombia.radius.element.Question;
import org.unlitrodeluzcolombia.radius.element.Survey;
import org.unlitrodeluzcolombia.radius.enums.QuestionType;
import org.unlitrodeluzcolombia.radius.facade.QuestionDAOFacade;
import org.unlitrodeluzcolombia.radius.facade.SurveyDAOFacade;
import org.unlitrodeluzcolombia.radius.gui.advertising.commons.QuestionFieldGenerator;
import org.unlitrodeluzcolombia.radius.gui.finder.CampaignFinder;
import org.unlitrodeluzcolombia.radius.web.facade.CampaignWebFacade;
import org.unlitrodeluzcolombia.radius.web.facade.SurveyWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Apr 10, 2019
 */
public class SurveyController extends AbstractComtorFacadeAdministratorControllerI18n<Survey, Long> {

    private static final Logger LOG = Logger.getLogger(SurveyController.class.getName());

    @Override
    public String getEntityName() {
        return "Encuestas";
    }

    @Override
    public String getLogModule() {
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
        description.addAttribute("required", "required");
        form.addField("Descripción", description, null);

        HtmlFinder campaign = getCampaignFinder(survey);
        form.addField("Campaña Publicitaria", campaign, "Indique a cuál campaña"
                + " publicitaria pertenece esta encuesta.", true);

        form.addSubTitle("Preguntas");

        HtmlDiv questions_area = new HtmlDiv("questions_area");

        if (survey != null) {
            QuestionDAOFacade daoFacade = new QuestionDAOFacade();
            LinkedList<Question> questions = new LinkedList<>();

            try {
                questions = daoFacade.findAllByProperty("survey", survey.getId());
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }

            QuestionFieldGenerator questionFieldGenerator = new QuestionFieldGenerator();
            questions.forEach((question) -> {
                questions_area.add(questionFieldGenerator.getQuestionTag(question.getType(),
                        question.getQuestion(), question.getOptions()));
            });
        }
        form.addRowInOneCell(questions_area);

        HtmlContainer container = new HtmlContainer();
        container.add(getAddQuestionButton("Agregar pregunta abierta",
                QuestionType.OPEN_QUESTION.toString(), "#D0F5A9"))
                .add(getAddQuestionButton("Agregar de selección simple",
                                QuestionType.SINGLE.toString(), "#A9E2F3"))
                .add(getAddQuestionButton("Agregar de selección múltiple",
                                QuestionType.MULTIPLE.toString(), "#D0A9F5"))
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
            HtmlText field = new HtmlText(survey.getDescription());
            form.addField("Descripción", field, null);

            field = new HtmlText(survey.getCampaign_description());
            form.addField("Campaña Publicitaria", field, null);
            
            form.addSubTitle("Preguntas");

            LinkedList<Question> questions = new QuestionDAOFacade()
                    .findAllByProperty("survey", survey.getId());

            if (!questions.isEmpty()) {
                int i = 1;

                for (Question question : questions) {
                    field = new HtmlText(question.getQuestion());
                    form.addField("Pregunta #" + i, field, null);

                    form.addField("", getOptionField(question), null);

                    i++;
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public HtmlElement getOptionField(Question question) {
        if (question.getType().equals(QuestionType.OPEN_QUESTION.toString())) {

            switch (question.getOptions()) {
                case "text":
                    HtmlInputText inputText = new HtmlInputText("options");
                    inputText.setReadOnly(false);
                    return inputText;
                case "phone":
                    HtmlInput inputPhone = new HtmlInput("phone", "options", "");
                    inputPhone.setReadOnly(false);
                    return inputPhone;
                case "date":
                    HtmlInput inputDate = new HtmlInput("date", "options", "");
                    inputDate.setReadOnly(false);
                    inputDate.addAttribute("pattern", "[0-9]{4}-[0-9]{2}-[0-9]{2}");
                    return inputDate;
                case "email":
                    HtmlInput inputEmail = new HtmlInput("email", "options", "");
                    inputEmail.setReadOnly(false);
                    return inputEmail;
                case "number":
                    HtmlInput inputNumber = new HtmlInput("number", "options", "");
                    inputNumber.setReadOnly(false);
                    return inputNumber;
            }
        }

        if (question.getType().equals(QuestionType.SINGLE.toString())) {
            HtmlRadioGroup options = new HtmlRadioGroup();
            for (String option : question.getOptionsArray()) {
                options.addElement(new HtmlRadio("options", option, option));
                options.addElement(new HtmlBr());
            }
            return options;
        }

        HtmlContainer container = new HtmlContainer();
        for (String option : question.getOptionsArray()) {
            container.addElement(new HtmlCheckbox("options", option, option));
            container.addElement(new HtmlBr());
        }
        return container;

    }

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
        headers.put("description", "Descripción");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Survey survey) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(survey.getDescription());

        return row;
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    @Override
    protected LinkedList<String> getBasicActionLinks(Survey object) {
        LinkedList<String> actions = new LinkedList<>();

        if ((getEditPrivilege() != null) && isEditable(object)
                && (SecurityHelper.can(getEditPrivilege(), getRequest()))) {
            actions.add(getEditIcon(getBaseUrl(), object));
        }

        if ((getDeletePrivilege() != null)
                && (SecurityHelper.can(getDeletePrivilege(), getRequest()))) {
            actions.add(getDeleteIcon(getBaseUrl(), object));
        }

        if ((getViewPrivilege() != null)
                && (SecurityHelper.can(getViewPrivilege(), getRequest()))) {
            actions.add(getViewIcon(getBaseUrl(), object));
        }

        return actions;
    }

    private boolean isEditable(Survey survey) {
        SurveyDAOFacade daoFacade = new SurveyDAOFacade();
        try {
            return daoFacade.haveAnswers(survey.getId());
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    private HtmlFinder getCampaignFinder(Survey zone) {
        Campaign campaign = null;
        String valueToShow = "";

        try {
            campaign = ((zone == null)
                    ? null
                    : new CampaignWebFacade().find(zone.getCampaign()));
            valueToShow = ((campaign == null)
                    ? ""
                    : new CampaignFinder().getValueToShow(campaign));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }

        return new HtmlFinder("campaign", CampaignFinder.class, valueToShow, 32);
    }

}
